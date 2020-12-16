package liangchen.wang.gradf.component.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class PaymentInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PaymentInitializer.class);
        springApplication.run(args);
    }
}
