package com.goudong.modules.simple.thread.p1;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class WaitingState implements Runnable{

    @Override
    public void run() {
        while(true) {
            synchronized (WaitingState.class) {
                try {
                    WaitingState.class.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
