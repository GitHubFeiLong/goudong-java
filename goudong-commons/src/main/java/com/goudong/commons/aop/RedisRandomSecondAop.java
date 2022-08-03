package com.goudong.commons.aop;

import com.goudong.commons.framework.redis.RedisTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 类描述：
 * RedisTool的切面，
 * 前置通知（Before）：在目标方法被调用之前调用通知功能
 * 后置通知（After）：在目标方法完成之后调用通知，此时不会关心方法的输出是什么
 * 返回通知（After-returning）：在目标方法成功执行之后调用通知
 * 异常通知（After-throwing）：在目标方法抛出异常后调用通知
 * 环绕通知（Around）：通知包裹了被通知的方法，在被通知的方
 * 法调用之前和之后执行自定义的行为
 *
 * 后置通知和返回通知的区别是，后置通知是不管方法是否有异常，都会执行该通知；而返回通知是方法正常结束时才会执行。
 * @author cfl
 * @date 2022/8/3 22:50
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedisRandomSecondAop {

	private final RedisTool redisTool;
	 /**
	  * 定义切点
	  */
	 @Pointcut(value = "@annotation(com.goudong.commons.annotation.aop.RedisRandomSecond)")
	 public void redisRandomSecond(){
	 	// aop 切点，避免代码检测报错问题
	 }

	/**
	 * 后置通知，用来删除ThreadLocal中的值,
	 */
	 @After("redisRandomSecond()")
	 public void after(JoinPoint joinPoint) {
		 redisTool.disableRandom();
	 }
}
