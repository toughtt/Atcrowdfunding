package junit.test;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wall
 * @data - 18:57
 */
public class TimerTest {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("I LOVE YOU");
            }
        }, 3000, 1000);


    }
}
