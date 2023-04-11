package com.goudong.boot.web.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.goudong.boot.web.core.BasicException;
import com.goudong.core.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 * 打印接口请求日志
 * @author msi
 * @date 2021/8/25 20:13
 * @version 1.0
 */
@Aspect
@Slf4j
@ConditionalOnClass(name = {"org.aspectj.lang.JoinPoint"})
public class ApiLogAop {

    private final Environment env;

    public ApiLogAop(Environment env) {
        if (log.isDebugEnabled()) {
            log.debug("注入apiLogAop");
        }
        this.env = env;
    }

    /**
     * 控制器
     */
    @Pointcut(
        "@within(org.springframework.web.bind.annotation.RestController)" +
        " || @within(org.springframework.stereotype.Controller)"
    )
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * 应用的控制层
     */
    @Pointcut("within(com.goudong.*.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }


    /**
     * Retrieves the {@link Logger} associated to the given {@link JoinPoint}.
     *
     * @param joinPoint join point we want the logger for.
     * @return {@link Logger} associated to the given {@link JoinPoint}.
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * 方法进入和退出时打印日志
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("springBeanPointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        Map<String, String> requestHead = getRequestHead(request);
        /*
            简单打印基础信息
         */
        log.debug("URI: {}; Method：{}； HeadParams：{}", requestURI, method, requestHead);

        StringBuffer sb = new StringBuffer();
        sb.append("\n-------------------------------------------------------------\n");
        sb.append("URI       : ").append(requestURI).append("\n");
        sb.append("Method    : ").append(method).append("\n");
        sb.append("HeadParams: ").append(requestHead).append("\n");
        ObjectMapper objectMapper = new ObjectMapper();
        // transient 忽略属性
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        // 修改时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        if (Objects.equals("GET", method)) {
            String params = objectMapper.writeValueAsString(request.getParameterMap());
            sb.append("Params    : ").append(params).append("\n");
        } else {
            // 过滤掉部分参数
            List<Object> args = getArgs(joinPoint);
            String params = objectMapper.writeValueAsString(args);
            sb.append("Params    : ").append(params).append("\n");
        }

        Object result = null;
        // 创建计时器
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            result = joinPoint.proceed();
        } catch (BasicException e) {
            result = e;
            logger(joinPoint).error(
                                "Exception in {}() with cause = \'{}\' and exception = \'{}\'",
                                joinPoint.getSignature().getName(),
                                e.getCause() != null ? e.getCause() : "NULL",
                                e.getMessage()
                        );

            throw e;
        } catch (Exception ex) {
            result = ex;
            logger(joinPoint).error(
                    "Exception in {}() with cause = \'{}\' and exception = \'{}\'",
                    joinPoint.getSignature().getName(),
                    ex.getCause() != null ? ex.getCause() : "NULL",
                    ex.getMessage()
            );
            throw ex;
        } finally {
            // 停止
            stopWatch.stop();
            // 忽略本次打印响应对象（默认不忽略）
            String s = objectMapper.writeValueAsString(result);
            if (s.length() <= 700) {
                sb.append("Results   : ").append(s).append("\n");
            }
            if (stopWatch.getTotalTimeSeconds() < 1) {
                sb.append("Time      : ").append(stopWatch.getTotalTimeMillis()).append("ms").append("\n");
            } else {
                sb.append("Time      : ").append(stopWatch.getTotalTimeSeconds()).append("s").append("\n");
            }

            sb.append("-------------------------------------------------------------\n");
            log.info(sb.toString());
        }

        return result;
    }

    /**
     * 获取请求参数
     * @param invocation
     * @return
     */
    private List<Object> getArgs(MethodInvocation invocation) {
        List<Class> filter = ListUtil.newArrayList(
                RequestFacade.class,
                ResponseFacade.class
        );
        Object[] argsArr = invocation.getArguments();
        // Stream.of(null).collect(Collectors.toList()) 会出现NPE
        if (argsArr != null && argsArr.length > 0) {
            log.debug("argsArr ＝{}", argsArr);
            // 过滤掉大对象，避免转json报错
            return Stream.of(argsArr)
                    // 过滤掉
                    .filter(f -> f != null && !filter.contains(f.getClass()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>(0);
    }

    /**
     * 获取请求参数
     * @param joinPoint
     * @return
     */
    private List<Object> getArgs(ProceedingJoinPoint joinPoint) {
        List<Class> filter = ListUtil.newArrayList(
                RequestFacade.class,
                ResponseFacade.class
        );
        Object[] argsArr = joinPoint.getArgs();
        // Stream.of(null).collect(Collectors.toList()) 会出现NPE
        if (argsArr != null && argsArr.length > 0) {
            log.debug("argsArr ＝{}", argsArr);
            // 过滤掉大对象，避免转json报错
            return Stream.of(argsArr)
                    // 过滤掉
                    .filter(f -> f != null && !filter.contains(f.getClass()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>(0);
    }

    /**
     * 获取x-开头的head参数和Authorization参数
     * @param request
     * @return
     */
    private Map<String, String> getRequestHead(HttpServletRequest request){
        //获取请求参数
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> data = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if(name.indexOf("x-")!=-1) {
                String value = request.getHeader(name);
                data.put(name, value);
            }
            if (Objects.equals(name.toLowerCase(), "authorization")) {
                data.put(name, request.getHeader(name));
            }
        }
        return data;
    }

}
