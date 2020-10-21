package liangchen.wang.gradf.component.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class CrawlerInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CrawlerInitializer.class);
        springApplication.run(args);
    }
}
