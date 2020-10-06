package liangchen.wang.gradf.component.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class CommonsInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CommonsInitializer.class);
        springApplication.run(args);
    }
}
