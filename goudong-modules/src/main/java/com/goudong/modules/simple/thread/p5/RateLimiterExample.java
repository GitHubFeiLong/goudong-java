package com.goudong.modules.simple.thread.p5;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 令牌桶算法
 * guava
 * @author chenf
 * @version 1.0
 */
public class RateLimiterExample {
    //~fields
    //==================================================================================================================
    // 创建一个桶容量为5，且每秒新增5个令牌，及每隔200毫秒新增一个令牌。
    private static RateLimiter rateLimiter = RateLimiter.create(5);

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws InterruptedException {
//        for (int i = 0; i < 100; i++) {
//
////            if (rateLimiter.tryAcquire(1, TimeUnit.MILLISECONDS)) {
////                System.out.println("i = " + i);
////            }
//
//            /*
//                limiter.acquire()
//                表示消费一个令牌，如果当前桶中有足够令牌则成功（返回值为0），如果桶中没有令牌则暂停一段时间，
//                比如发令牌间隔是200毫秒，则等待200毫秒后再去消费令牌。
//             */
//            double acquire = rateLimiter.acquire();
//            System.out.println("acquire = " + acquire + ", i = " + i);
//        }

        //
//        System.out.println(rateLimiter.acquire(10)); // 突发获取十个令牌（原桶中只有5个）
//        System.out.println(rateLimiter.acquire(1)); // 1.999188 需要等待接近2秒
//        System.out.println(rateLimiter.acquire(1));

        Thread.sleep(20000);
        System.out.println(rateLimiter.acquire(5));
        System.out.println(rateLimiter.acquire(5));
        System.out.println(rateLimiter.acquire(5));
    }
}
