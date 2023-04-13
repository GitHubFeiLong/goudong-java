package com.goudong.commons.aop;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.goudong.commons.constant.core.SpringProfileConst;
import com.goudong.core.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 * 根据配置的环境，打印不同级别的日志
 * 该类复制自JHipster中的源码
 * @author msi
 * @date 2021/8/25 20:13
 * @version 1.0
 */
@Slf4j
@Aspect
public class LoggingAop {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        // transient 忽略属性
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        // 修改时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    private final Environment env;

    public LoggingAop(Environment env) {
        if (log.isDebugEnabled()) {
            log.debug("注入loggingAop");
        }
        this.env = env;
    }

    /**
     * 注解的切点
     * mapper注解不生效，直接切mapper所在的包
     */
    @Pointcut(
        "@within(org.springframework.stereotype.Service)"
    )
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * 业务层，持久层的包切点
     */
    @Pointcut(
        "within(com.goudong.*.service..*)"
    )
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
     * 方法发生异常打印日志
     *
     * @param joinPoint join point for advice.
     * @param e exception.
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (
                env.acceptsProfiles(Profiles.of(SpringProfileConst.DEVELOPMENT)) ||
                env.acceptsProfiles(Profiles.of(SpringProfileConst.TEST))
        ) {
            logger(joinPoint)
                .error(
                    "Exception in {}() with cause = \'{}\' and exception = \'{}\'",
                    joinPoint.getSignature().getName(),
                    e.getCause() != null ? e.getCause() : "NULL",
                    e.getMessage()
                );
        } else {
            logger(joinPoint)
                .error(
                    "Exception in {}() with cause = {}",
                    joinPoint.getSignature().getName(),
                    e.getCause() != null ? e.getCause() : "NULL"
                );
        }
    }

    /**
     * 方法进入和退出时打印日志
     * @Around("springBeanPointcut()||mapperPackagePointcut()") 这样能进入controller以及mapper
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("springBeanPointcut() && applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);

        // 过滤掉部分参数
        List<Object> args = getArgs(joinPoint);
        String params;
        try {
            params = objectMapper.writeValueAsString(args);
        } catch (Exception e) {
            log.warn("对象序列化json失败：{}", e);
            params = args.toString();
        }

        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), params);
        }
        Object result = joinPoint.proceed();
        String resultStr;
        try {
            resultStr = objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            log.warn("对象序列化json失败：{}", e);
            resultStr = args.toString();
        }

        if (log.isDebugEnabled()) {
            log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), resultStr);
        }
        return result;
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
}
