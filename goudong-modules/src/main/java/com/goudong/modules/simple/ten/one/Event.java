package com.goudong.modules.simple.ten.one;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 *
 * @author chenf
 */
public abstract class Event {
    //~fields
    //==================================================================================================================
    private long eventTime;

    protected final long delayTime;

    public Event(long delayTime) {
        this.delayTime = delayTime;
        start();
    }

    public void start() {
        eventTime = System.nanoTime() + delayTime;
    }

    public boolean ready() {
        int a = 10;

        class Inner {
            void print() {
                System.out.println("a = " + a);
            }
        }

        return System.nanoTime() >= eventTime;

    }
    //~methods
    //==================================================================================================================

    public abstract void action();

    public static void main(String[] args) {
        int a = 10;
        class Inner {
            void print() {
                System.out.println("a = " + a);
            }
        }

        Inner inner = new Inner();
        inner.print();

        Event event = new Event(10) {

            @Override
            public void action() {
            }
        };


    }
}
