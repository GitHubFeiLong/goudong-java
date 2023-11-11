package com.goudong.modules.simple.thread.p1.sort;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class ThreadSort01 {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1");
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2");
        });
        Thread thread3 = new Thread(() -> {
            System.out.println("thread3");
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
