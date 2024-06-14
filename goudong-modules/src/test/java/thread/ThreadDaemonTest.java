package thread;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @author chenf
 */
public class ThreadDaemonTest {
    //~fields
    //==================================================================================================================
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        System.out.println("main end");
    }

    //~methods
    //==================================================================================================================
    public static class MyThread extends Thread {

        public MyThread() {
            setDaemon(true);
        }


        @Override
        public void run() {
            try {
                System.out.println("MyThread start running");
                TimeUnit.SECONDS.sleep(1L);
                System.out.println("MyThread end run");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
