package com.goudong.modules.simple.thread.p1;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class ThreadState {
    public static void main(String[] args) {
        System.out.println(123);
        new Thread(new WaitingTime(), "WaitingTimeThread").start();
        new Thread(new WaitingState(), "WaitingStateThread").start();
        //BlockedThread-01线程会抢到锁，BlockedThread-02线程会阻塞
        new Thread(new BlockedThread(), "BlockedThread-01").start();
        new Thread(new BlockedThread(), "BlockedThread-02").start();
    }
}
