package com.goudong.commons.aop;

import com.goudong.commons.constant.SpringProfileConst;
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

import java.util.Arrays;

/**
 * 类描述：
 * 根据配置的环境，打印不同级别的日志
 * 该类复制自JHipster中的源码
 * @author msi
 * @date 2021/8/25 20:13
 * @version 1.0
 */
@Aspect
// @Component
public class LoggingAop {

    private final Environment env;

    public LoggingAop(Environment env) {
        this.env = env;
    }

    /**
     * @RestController，@Controller,@Service 注解的切点
     * mapper注解不生效，直接切mapper所在的包
     */
    @Pointcut(
        "@within(org.springframework.stereotype.Service)" +
        " || @within(org.springframework.web.bind.annotation.RestController)" +
        " || @within(org.springframework.stereotype.Controller)" +
        " || within(com.goudong.*.mapper..*)"
    )
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * 应用的控制层，业务层，持久层的包切点
     */
    @Pointcut(
        "within(com.goudong.*.controller..*)" +
        " || within(com.goudong.*.service..*)" +
        " || within(com.goudong.*.mapper..*)"
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
    @Around("springBeanPointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        Object result = joinPoint.proceed();
        if (log.isDebugEnabled()) {
            log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);
        }
        return result;
    }

}
