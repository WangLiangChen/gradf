package liangchen.wang.gradf.component.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class ConfigInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ConfigInitializer.class);
        springApplication.run(args);
    }
}
