package com.goudong.modules.simple.thread.p2;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class CyclicBarrierExample {
    //~fields
    //==================================================================================================================
    private static final int THREAD_COUNT = 20;

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadNum = i;
            Thread.sleep(1000);
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("线程" + threadNum + "到达栅栏");
                        cyclicBarrier.await();
                        Thread.sleep(1000);
                        System.out.println("线程" + threadNum + "离开栅栏");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        exec.shutdown();

    }
}
