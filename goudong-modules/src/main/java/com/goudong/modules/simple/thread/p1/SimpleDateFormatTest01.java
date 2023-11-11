package com.goudong.modules.simple.thread.p1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class SimpleDateFormatTest01 {
    //~fields
    //==================================================================================================================
    private static final int EXECUTE_COUNT = 1000;
    private static final int THREAD_COUNT = 10;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static Lock lock = new ReentrantLock();

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws InterruptedException {
        final Semaphore semaphore = new Semaphore(THREAD_COUNT);
        final CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);
        ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>(){
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("2020-01-01");
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    try {
                        threadLocal.get().parse("2020-01-01");
//                        lock.lock();
//                        simpleDateFormat.parse("2020-01-01");
//                        synchronized (simpleDateFormat) {
//                            simpleDateFormat.parse("2020-01-01");
//                        }

                    } catch (ParseException e) {
                        System.out.println("线程：" + Thread.currentThread().getName() + "格式化日期失败");
                        e.printStackTrace();
                        System.exit(1);
                    } catch (NumberFormatException e) {
                        System.out.println("线程：" + Thread.currentThread().getName() + " 格式化日期失 败");
                        e.printStackTrace();
                        System.exit(1);
                    } finally {
//                        lock.unlock();
                    }
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println("信号量发送错误");
                    e.printStackTrace();
                    System.exit(1);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("所有线程格式化日期成功");
    }
}
