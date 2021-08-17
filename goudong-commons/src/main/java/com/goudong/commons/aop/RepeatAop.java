package com.goudong.commons.aop;

import com.goudong.commons.annotation.Repeat;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import com.goudong.commons.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
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
	private RedisOperationsUtil redisOperationsUtil;

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
		String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
		// 没有携带token，直接放行
		if (StringUtils.isBlank(token)) {
			return pjp.proceed();
		}

		// 获取注解的参数
		MethodSignature signature = (MethodSignature)pjp.getSignature();
		Repeat repeat = signature.getMethod().getAnnotation(Repeat.class);

		Object ret = null;

		//

		Long userId = JwtTokenUtil.getUserId(request);

		// 格式化请求url，将其参数路径变为*
		String path = StringUtil.replacePathVariable2Asterisk(request.getRequestURI());

		String value = redisOperationsUtil.getStringValue(RedisKeyEnum.REPEAT_URI, path, userId.toString());
		// redis中没有指定key，符合条件则继续执行，否则终止方法的执行
		if (value == null) {
			// 将key存在redis中，指定时间
			redisOperationsUtil.setStringValue(RedisKeyEnum.REPEAT_URI, "1", repeat.time(), repeat.timeUnit(), request.getRequestURI(), userId.toString());
			// 执行方法
			ret =  pjp.proceed();
		} else {
			log.warn("防止了 {} 重复提交",request.getRequestURI());
			// 429
			BasicException.exception(ClientExceptionEnum.TOO_MANY_REQUESTS);
		}

		// 停止执行
		return ret;
	}
}
