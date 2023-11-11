package com.goudong.modules.simple.thread.p1;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class BlockedThread implements Runnable{

    @Override
    public void run() {
        synchronized (BlockedThread.class){
            while (true) {
                WaitingTime.waitSecond(100);
            }
        }
    }
}
