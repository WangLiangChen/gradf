package liangchen.wang.gradf.framework.cluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LiangChen.Wang
 */
@SpringBootApplication
public class ClusterInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ClusterInitializer.class);
        springApplication.run(args);
    }
}
