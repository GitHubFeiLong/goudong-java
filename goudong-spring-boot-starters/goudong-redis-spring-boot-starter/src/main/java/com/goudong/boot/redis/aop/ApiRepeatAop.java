package com.goudong.boot.redis.aop;

import com.goudong.boot.redis.context.User;
import com.goudong.boot.redis.context.UserContext;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.redis.core.SimpleRedisKey;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 使用AOP防止用户重复提交表单，用户将参数进行修改后再次提交不会锁住
 *
 * @see ApiRepeat
 * @Author msi
 * @Date 2020/6/10 19:17
 * @Version 1.0
 */
@Slf4j
@Aspect
public class ApiRepeatAop {

	@Resource
	private HttpServletRequest request;

	@Resource
	private RedisTool redisTool;

	@Resource
	private RedissonClient redissonClient;

	// public ApiRepeatAop(HttpServletRequest request, RedisTool redisTool, RedissonClient redissonClient) {
	// 	this.request = request;
	// 	this.redisTool = redisTool;
	// 	this.redissonClient = redissonClient;
	// }

	/**
	 * 定义切点
	 */
	@Pointcut(value = "@annotation(com.goudong.boot.redis.aop.ApiRepeat)")
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

		User user = UserContext.get();
		String sessionId;
		// 用户对象必须有值（只要进入网关的请求，都会有值,如果连用户都分不开就没有必要做限流了 对吧）
		if (user != null && StringUtil.isNotBlank(sessionId = user.getSessionId())) {
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
			String redisKey = SimpleRedisKey.API_REPEAT_KEY.getFullKey(sessionId, requestData);


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
			if (log.isDebugEnabled()) {
				log.debug("sessionId:{}, api:{}, 重复请求已被阻止", sessionId, request.getRequestURI());
			}

			throw new RuntimeException("429 Too Many Requests");
		}

		/*
			用户sessionId获取失败
		 */
		ret = pjp.proceed();
		log.error("获取请求会话id失败，用户无效或会话id无效");

		// 停止执行
		return ret;
	}
}
