package liangchen.wang.gradf.component.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class JobInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(JobInitializer.class);
        springApplication.run(args);
    }
}
