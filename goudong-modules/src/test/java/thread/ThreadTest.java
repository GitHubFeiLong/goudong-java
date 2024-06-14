package thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @author chenf
 */
@ExtendWith({})
public class ThreadTest {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    @DisplayName("join")
    void testJoin() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println("thread 1");
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            try {
                thread1.join();
                // TimeUnit.MILLISECONDS.sleep(100);
                System.out.println("thread 2");
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread2.start();

        thread2.join();
        thread1.join();
    }
}
