package liangchen.wang.gradf.component.merchant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class MerchantInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MerchantInitializer.class);
        springApplication.run(args);
    }
}
