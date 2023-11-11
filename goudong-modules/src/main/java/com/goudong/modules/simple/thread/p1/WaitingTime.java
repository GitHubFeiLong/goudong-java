package com.goudong.modules.simple.thread.p1;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class WaitingTime implements Runnable{

    @Override
    public void run() {
        while (true) {
            waitSecond(200);
        }
    }

    public static void waitSecond(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
