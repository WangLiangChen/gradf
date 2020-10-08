package liangchen.wang.gradf.component.foura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class FouraInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FouraInitializer.class);
        springApplication.run(args);
    }
}
