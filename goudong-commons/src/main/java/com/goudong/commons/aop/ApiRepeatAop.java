package com.goudong.commons.aop;

import com.goudong.commons.core.context.UserContext;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.framework.redis.GenerateRedisKeyUtil;
import com.goudong.commons.framework.redis.RedisTool;
import com.goudong.commons.framework.redis.SimpleRedisKey;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.exception.core.ClientException;
import com.goudong.exception.enumerate.ClientExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 使用AOP防止用户重复提交表单，用户将参数进行修改后再次提交不会锁住
 *
 * @see com.goudong.commons.annotation.aop.ApiRepeat
 * @Author msi
 * @Date 2020/6/10 19:17
 * @Version 1.0
 */
@Slf4j
@Aspect
@ConditionalOnClass(value = {RedisTool.class, RedissonClient.class})
public class ApiRepeatAop {

	private final HttpServletRequest request;

	private final RedisTool redisTool;

	private final RedissonClient redissonClient;
	public ApiRepeatAop(HttpServletRequest request, RedisTool redisTool, RedissonClient redissonClient) {
		this.request = request;
		this.redisTool = redisTool;
		this.redissonClient = redissonClient;
	}

	/**
	 * 定义切点
	 */
	@Pointcut(value = "@annotation(com.goudong.commons.annotation.aop.ApiRepeat)")
	public void apiRepeat() {
		// aop 切点，避免代码检测报错问题
	}

	/**
	 * 使用环绕通知可以阻止程序继续执行
	 *
	 * @param pjp
	 * @throws Throwable
	 */
	@Around("apiRepeat()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		// 被切方法的返回值
		Object ret = null;
		BaseUserDTO baseUserDTO = UserContext.get();
		String sessionId;
		// 用户对象必须有值（只要进入网关的请求，都会有值,如果连用户都分不开就没有必要做限流了 对吧）
		if (baseUserDTO != null && StringUtils.isNotBlank(sessionId = baseUserDTO.getSessionId())) {
			// 当前请求url

			// 接口参数
			Object[] args = pjp.getArgs();
			StringBuilder methodParameter = new StringBuilder();
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					methodParameter.append(args[i] == null ? "null" : args[i].toString());
				}
			}

			// 获取请求路径
			StringBuilder requestData = new StringBuilder()
					.append(request.getMethod())
					.append(request.getRequestURI())
					.append(methodParameter);

			// 因为功能只是进行控制访问频率，所以以用户和请求路径为key
			String redisKey = GenerateRedisKeyUtil.generateByClever(SimpleRedisKey.API_REPEAT_KEY, sessionId, requestData);


			// 获取注解的参数
			//Class clazz = pjp.getTarget().getClass();
			//MethodSignature ms = (MethodSignature)pjp.getSignature();
			//Method targetMethod = clazz.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
			//使用spring的工具类，能使用@AliasFor特性
			//ApiRepeat apiRepeat = AnnotationUtils.getAnnotation(targetMethod, ApiRepeat.class);

			RLock lock = redissonClient.getLock(redisKey);
			// 尝试
			boolean tryLock = lock.tryLock();
			if (tryLock) {
				// 获取锁成功，执行方法
				try {
					ret = pjp.proceed();
				} finally {
					// 执行完成，释放缓存
					lock.unlock();
				}
				return ret;
			}

			// 获取锁失败
			LogUtil.debug(log, "sessionId:{}, api:{}, 重复请求已被阻止", sessionId, request.getRequestURI());
			throw ClientException.client(ClientExceptionEnum.TOO_MANY_REQUESTS);
		}

		/*
			用户sessionId获取失败
		 */
		ret = pjp.proceed();
		LogUtil.error(log, "获取请求会话id失败，用户无效或会话id无效");

		// 停止执行
		return ret;
	}
}
