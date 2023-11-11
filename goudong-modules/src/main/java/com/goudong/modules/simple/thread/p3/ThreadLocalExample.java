package com.goudong.modules.simple.thread.p3;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class ThreadLocalExample {
    //~fields
    //==================================================================================================================
    public static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        threadLocal.set("parent value");
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("sub threadLocal value = " + threadLocal.get());
        });
        t.start();

        System.out.println("threadLocal.get() = " + threadLocal.get());
    }
}
