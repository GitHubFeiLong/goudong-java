package com.goudong.modules.simple.thread.p2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class CountDownLatchExample {
    //~fields
    //==================================================================================================================
    private static final int THREAD_COUNT = 200;
    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadNum = i;
            exec.execute(() -> {
                try {
                    test(threadNum);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }

            });
        }

//        countDownLatch.await();
        countDownLatch.await(10, TimeUnit.SECONDS);
        System.out.println("finish");
        exec.shutdown();

    }

    private static void test(int threadNum) throws InterruptedException {
        Thread.sleep(100);
        System.out.println("threadNum:" + threadNum);
        Thread.sleep(100);
    }
}
