package com.goudong.commons.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.goudong.commons.utils.IpAddressUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 类描述：
 *  日志切面：记录请求的参数，返回的结果
 *  暂时不适用该日志类
 * @ClassName LogAOP
 * @Author msi
 * @Date 2020/11/4 16:13
 * @Version 1.0
 */
@Slf4j
@Aspect
///@Component
public class LogAop {
    /**
     * 保存request中的代码执行开始时间戳
     */
    public static final String REQUEST_ATTRIBUTE_KEY = "log-start-time";

    /**
     * 注入请求对象
     */
    @Resource
    private HttpServletRequest request;
    /**
     * 切点
     */
    @Pointcut(value = "execution(* com.goudong.*.controller..*.*(..))")
    public void method(){
        throw new UnsupportedOperationException();
    };

    /**
     * 异常通知
     * 这里的异常使用Throwable，这样就算应用出现任何错误都会打印日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "method()", throwing = "e")
    public void afterThrowing (JoinPoint joinPoint, Throwable e) {
        // 获取日志对象
        LogInfoDTO logInfoDTO = this.generateLogInfoDTO(joinPoint);
        // 设置返回值
        logInfoDTO.setReturnValue(e);
        // 打印日志
        this.outErrorLogInfoDTO(logInfoDTO);

    }

    /**
     * 输出错误日志
     * @param logInfoDTO
     */
    private void outErrorLogInfoDTO(LogInfoDTO logInfoDTO) {
        try {
            // 当JSON.toJSONString 报错时，使用toString方法
            log.error(JSON.toJSONString(logInfoDTO));
        } catch (Exception ex) {
            log.error(logInfoDTO.toString());
        }
    }

    /**
     * 环绕通知
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "method()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        // 设置日志的开始时间
        this.request.setAttribute(LogAop.REQUEST_ATTRIBUTE_KEY, start);
        // 继续执行方法
        Object obj = pjp.proceed();

        // 打印日志
        LogInfoDTO logInfoDTO = generateLogInfoDTO(pjp);
        logInfoDTO.setReturnValue(obj);
        // 打印正常的日志
        this.outInfoLogInfoDTO(logInfoDTO);

        return obj;
    }

    /**
     * 输出正常处理请求日志
     * @param logInfoDTO
     */
    private void outInfoLogInfoDTO(LogInfoDTO logInfoDTO) {
        try {
            // 当JSON.toJSONString 报错时，使用toString方法
            log.info(JSON.toJSONString(logInfoDTO));
        } catch (Exception e) {
            log.info(logInfoDTO.toString());
        }
    }

    /**
     * 生成日志对象
     * @param joinPoint 连接点
     * @return
     */
    private LogInfoDTO generateLogInfoDTO(JoinPoint joinPoint) {
        // 不报错就开始处理日志
        String ipAddress = IpAddressUtil.getIpAddress(request);
        // 参数值
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        // 参数
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        Class[] parameterTypes = methodSignature.getParameterTypes();
        // 方法参数
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0, length = args.length; i < length; i++) {
            if (Objects.nonNull(args[i])) {
                // 参数
                parameters.put(parameterTypes[i].getTypeName()+ "." + parameterNames[i], args[i]);
            }
        }

        // 构建一个logindo对象
        LogInfoDTO logInfoDTO = LogInfoDTO.builder().threadId(Thread.currentThread().getId())
                .requestIpAddress(ipAddress)
                .requestURI(request.getRequestURI())
                .requestType(request.getMethod())
                .parameters(parameters).build();

        // 获取接口开始运行的时间戳
        Object start = this.request.getAttribute(LogAop.REQUEST_ATTRIBUTE_KEY);
        if (start instanceof Long) {
            logInfoDTO.setTimeConsuming(System.currentTimeMillis() - (long)start);
        }

        return logInfoDTO;
    }




}

/**
 * 类描述：
 *  记录日志信息
 * @ClassName LogInfoDTO
 * @Author msi
 * @Date 2020/11/15 11:21
 * @Version 1.0
 */
 @Builder
 @Data
 @ApiModel
 @JSONType(orders={"requestIpAddress","requestURI","requestType","threadId","parameters","returnValue","timeConsuming"})
 class LogInfoDTO {
    @ApiModelProperty(name = "requestIpAddress", value = "请求ip地址", example = "192.168.1.100")
    private String requestIpAddress;
    @ApiModelProperty(name = "requestURI", value = "请求接口地址", example = "192.168.1.100")
    private String requestURI;
    @ApiModelProperty(name = "requestType", value = "请求类型", example = "GET")
    private String requestType;
    @ApiModelProperty(name = "threadId", value = "线程id", example = "1")
    private Long threadId;
    @ApiModelProperty(name = "timeConsuming", value = "请求用时，单位毫秒", example = "1000")
    private Long timeConsuming;
    @ApiModelProperty(name = "parameters", value = "请求参数")
    private Map<String, Object> parameters;
    @ApiModelProperty(name = "returnValue", value = "请求结果", example = "User")
    private Object returnValue;
}
