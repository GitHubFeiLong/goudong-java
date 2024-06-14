package com.goudong.modules.simple.thread.deadlock.demo1;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 表示一个哲学家对象，参与进餐和思考活动。每个哲学家都有两个筷子（左和右）以及一个思考因子。
 *
 * @author chenf
 */
public class Philosopher implements Runnable {

    //~fields
    //==================================================================================================================
    // 哲学家使用的左筷子
    private final Chopstick left;

    // 哲学家使用的右筷子
    private final Chopstick right;

    // 哲学家的唯一标识符
    private final int id;

    // 哲学家的思考因子，用于控制思考和进餐的时间比例
    private final int ponderFactor;

    // 随机数生成器，用于模拟随机的思考时间
    private final Random rand = new Random(47);

    /**
     * 暂停一段时间，模拟思考或进餐的动作。时间长度由ponderFactor随机决定。
     *
     * @throws InterruptedException 如果线程被中断
     */
    private void pause() throws InterruptedException {
        if (ponderFactor == 0) {
            return;
        }
        // 根据思考因子随机决定暂停时间，范围在0到ponderFactor*250毫秒之间
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
    }

    /**
     * 构造函数，初始化哲学家对象。
     *
     * @param left   哲学家的左筷子
     * @param right  哲学家的右筷子
     * @param ident  哲学家的唯一标识符
     * @param ponder 哲学家的思考因子
     */
    public Philosopher(Chopstick left, Chopstick right, int ident, int ponder) {
        this.left = left;
        this.right = right;
        id = ident;
        ponderFactor = ponder;
    }

    //==================================================================================================================
    // 当线程运行时，哲学家会循环进行思考、拿起筷子、进餐、放下筷子的动作。
    @Override
    public void run() {
        try {
            // 在线程被中断之前，无限循环进行思考和进餐的动作
            while (!Thread.interrupted()) {
                // 模拟思考过程
                System.out.println(this + " " + "thinking");
                pause();

                // 尝试拿起右筷子
                System.out.println(this + " " + "grabbing right");
                right.take();

                // 尝试拿起左筷子
                System.out.println(this + " " + "grabbing left");
                left.take();

                // 模拟进餐过程
                System.out.println(this + " " + "eating");
                pause();

                // 放下筷子
                right.drop();
                left.drop();
            }
        } catch (InterruptedException e) {
            // 如果线程被中断，打印信息并退出
            System.out.println(this + " " + "exiting via interrupt");
        }
    }

    /**
     * 返回哲学家的字符串表示，包含其标识符。
     *
     * @return 哲学家的字符串表示
     */
    @Override
    public String toString() {
        return "Philosopher " + id;
    }
}
