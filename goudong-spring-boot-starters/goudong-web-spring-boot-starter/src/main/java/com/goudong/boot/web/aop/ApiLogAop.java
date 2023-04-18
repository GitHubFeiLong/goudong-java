package com.goudong.boot.web.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.web.core.ApiLog;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.properties.ApiLogProperties;
import com.goudong.boot.web.util.IpUtil;
import com.goudong.boot.web.util.TraceIdUtil;
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

    private final ObjectMapper objectMapper;

    private final ApiLogProperties apiLogProperties;

    public ApiLogAop(Environment env, ObjectMapper objectMapper, ApiLogProperties apiLogProperties) {
        if (log.isDebugEnabled()) {
            log.debug("注入apiLogAop");
        }
        this.env = env;
        this.objectMapper = objectMapper;
        this.apiLogProperties = apiLogProperties;
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
        // 计时器，当是开启接口打印时才创建对象，提高程序性能
        StopWatch stopWatch = null;
        ApiLog apiLog = null;
        if (apiLogProperties.getEnabled()) {
            stopWatch = new StopWatch();  // 创建计时器
            stopWatch.start();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = IpUtil.getStringIp(request);
            String requestURI = request.getRequestURI();
            String method = request.getMethod();
            Map<String, String> requestHead = getRequestHead(request);
            Object params = Objects.equals("GET", method) ? request.getParameterMap() : getArgs(joinPoint);

            // 创建apiLog对象
            apiLog = new ApiLog();
            apiLog.setIp(ip);
            apiLog.setUri(requestURI);
            apiLog.setMethod(method);
            apiLog.setHeadParams(requestHead);
            apiLog.setParams(params);
        }

        Object result = null;                   // 接口返回值
        boolean successful = false;              // 是否执行成功
        try {
            result = joinPoint.proceed();       // 执行方法
            successful = true;                   // 设置本次执行成功
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

            if (apiLogProperties.getEnabled()) {
                stopWatch.stop();
                long time = stopWatch != null ? stopWatch.getTotalTimeMillis() : -1;
                apiLog.setResults(result);

                apiLog.setTranceId(TraceIdUtil.get());
                apiLog.setSuccessful(successful);
                apiLog.setTime(time);
                // 输出接口日志
                apiLog.printLogString(apiLogProperties, objectMapper);
            }
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
        ApiLogProperties.PrintLogLimit printLogLimit = apiLogProperties.getPrintLogLimit();
        // 自定义配置的请求头限制
        Map<String, Boolean> headParams = printLogLimit.getHeadParams();
        while (headerNames.hasMoreElements()) {
            // 请求头转小写
            String name = headerNames.nextElement().toLowerCase();
            if (headParams.containsKey(name) && headParams.get(name)) {
                data.put(name, request.getHeader(name));
                continue;
            }
            if (name.indexOf("x-") != -1 && !headParams.containsKey(name)) {
                data.put(name, request.getHeader(name));
                continue;
            }

        }
        return data;
    }

}
