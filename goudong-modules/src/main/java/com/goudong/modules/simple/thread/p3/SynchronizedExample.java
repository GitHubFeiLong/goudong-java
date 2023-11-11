package com.goudong.modules.simple.thread.p3;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class SynchronizedExample {
    //~fields
    //==================================================================================================================
    /**
     * 监视对象
     */
    private Object obj = new Object();

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws InterruptedException {
        SynchronizedExample example = new SynchronizedExample();
        example.run();
    }

    public void run() throws InterruptedException {
        synchronized (obj) {
            System.out.println("test run()");
        }
    }

    public synchronized void execute() {
        System.out.println("test execute()");
    }

    public synchronized static void submit() {
        System.out.println("test submit()");
    }
}
