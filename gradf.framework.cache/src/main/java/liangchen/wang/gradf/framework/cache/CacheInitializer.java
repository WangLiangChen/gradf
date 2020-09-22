package liangchen.wang.gradf.framework.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class CacheInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CacheInitializer.class);
        springApplication.run(args);
    }
}
