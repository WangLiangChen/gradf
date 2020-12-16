package liangchen.wang.gradf.component.bpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class BpmInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BpmInitializer.class);
        springApplication.run(args);
    }
}
