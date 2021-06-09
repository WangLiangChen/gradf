package liangchen.wang.gradf.framework.data.test;

import liangchen.wang.gradf.framework.data.annotation.EnableJdbc;
import liangchen.wang.gradf.framework.data.manager.ISequenceManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableJdbc
public class DataTest {
    @Inject
    private ISequenceManager sequenceManager;

    @Test
    public void testSecquenceNumber() throws InterruptedException {
        int count = 40;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count / 4; i++) {
            new Thread(() -> {
                Long number = sequenceManager.sequenceNumber("w1");
                System.out.println("w1==========>" + number);
                countDownLatch.countDown();
            }).start();
            new Thread(() -> {
                Long number = sequenceManager.sequenceNumber("w3");
                System.out.println("w3==========>" + number);
                countDownLatch.countDown();
            }).start();
            new Thread(() -> {
                Long number = sequenceManager.sequenceNumber("w5");
                System.out.println("w5==========>" + number);
                countDownLatch.countDown();
            }).start();
            new Thread(() -> {
                Long number = sequenceManager.sequenceNumber("w4");
                System.out.println("w4==========>" + number);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }

}