package com.goudong.modules.simple.thread.deadlock.demo1;

/**
 * Chopstick 类表示一根筷子，用于模拟哲学家问题。
 * 哲学家问题是一个经典的多线程问题，描述了五个哲学家围坐在一张桌子旁，每个人面前都有一根筷子。
 * 他们既可以拿起筷子吃饭，也可以思考。问题的目的是防止所有哲学家同时拿起筷子，从而导致死锁。
 */
public class Chopstick {
    /**
     * 筷子的状态，表示筷子是否被拿起。
     */
    private boolean taken = false;

    /**
     * 拿起筷子。
     * 如果筷子已被拿起，则当前线程等待，直到筷子被放下。
     * 此方法是同步的，确保了同时只有一个线程可以拿起筷子。
     *
     * @throws InterruptedException 如果线程在等待时被中断。
     */
    public synchronized void take() throws InterruptedException{
        while (taken) {
            wait();
        }
        taken = true;
    }

    /**
     * 放下筷子。
     * 当前线程放下筷子后，如果有其他线程正在等待拿起筷子，它们将被唤醒。
     * 此方法是同步的，确保了同时只有一个线程可以放下筷子，并且正确地通知其他等待线程。
     */
    public synchronized void drop() {
        taken = false;
        notifyAll();
    }
}
