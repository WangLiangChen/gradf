package liangchen.wang.gradf.component.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class ComponentCommonsInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ComponentCommonsInitializer.class);
        springApplication.run(args);
    }
}
