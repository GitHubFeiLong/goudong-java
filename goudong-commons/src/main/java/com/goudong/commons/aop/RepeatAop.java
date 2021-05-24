package com.goudong.commons.aop;

import com.goudong.commons.annotation.Repeat;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.utils.RedisValueUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 类描述：
 * 使用AOP防止用户重复提交表单
 * @Author msi
 * @Date 2020/6/10 19:17
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class RepeatAop {

	@Resource
	private RedisValueUtil redisValueUtil;

	/**
	 * 定义切点：使用了注解@RepeatSubmitAnnotation的所有方法和类
	 */
	@Pointcut(value = "@annotation(com.goudong.commons.annotation.Repeat)")
	public void repeat(){
		// aop 切点，避免代码检测报错问题
	}


	/**
	 * 使用环绕通知可以阻止程序继续执行
	 * @param pjp
	 * @throws Throwable
	 */
	@Around("repeat()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		//获取request和response对象
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		assert Objects.nonNull(attributes) : "ServletRequestAttributes 为空了";
		HttpServletRequest request = attributes.getRequest();

		// 获取注解的参数
		MethodSignature signature = (MethodSignature)pjp.getSignature();
		Repeat repeat = signature.getMethod().getAnnotation(Repeat.class);

		Object ret = null;

		String userUuid= JwtTokenUtil.getUserUuid(request);

		Object value = redisValueUtil.getValue(RedisKeyEnum.REPEAT_URI, request.getRequestURI(), userUuid);

		// redis中没有指定key，符合条件则继续执行，否则终止方法的执行
		if (ObjectUtils.isEmpty(value)) {
			// 将key存在redis中，指定时间
			redisValueUtil.setValue(RedisKeyEnum.REPEAT_URI, 1, repeat.time(), repeat.timeUnit(), request.getRequestURI(), userUuid);
			// 执行方法
			ret =  pjp.proceed();
		} else {
			log.info("防止了 {} 重复提交",request.getRequestURI());
			// 429
			BasicException.exception(ClientExceptionEnum.TOO_MANY_REQUESTS);
		}
		// 停止执行
		return ret;
	}
}
