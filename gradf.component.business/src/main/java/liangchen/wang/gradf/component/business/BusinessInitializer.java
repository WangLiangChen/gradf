package liangchen.wang.gradf.component.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class BusinessInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BusinessInitializer.class);
        springApplication.run(args);
    }
}
