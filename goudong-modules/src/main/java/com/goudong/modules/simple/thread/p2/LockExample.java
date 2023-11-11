package com.goudong.modules.simple.thread.p2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.StampedLock;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class LockExample {
    //~fields
    //==================================================================================================================
    public static int clientTotal = 5000;

    public static int threadTotal = 200;

    public static int count = 0;

    private static final StampedLock lock = new StampedLock();
    //~methods
    //==================================================================================================================

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exes = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            exes.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                countDownLatch.countDown();

            });
        }

        countDownLatch.await();
        exes.shutdown();
        System.out.println("count = " + count);

    }

    public static void add() {
        // 加锁时返回票据
        long l = lock.writeLock();
        try {
            count++;
        } finally {
            // 释放锁时带上票据
            lock.unlock(l);
        }
    }

}
