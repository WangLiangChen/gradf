package liangchen.wang.gradf.framework.cluster.annotation;

import liangchen.wang.gradf.framework.cluster.configuration.RedisLockAutoConfiguration;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@Import({EnableRedisShedLock.ShedLockImportSelector.class})
public @interface EnableRedisShedLock {
    class ShedLockImportSelector implements ImportSelector {
        private static boolean loaded = false;

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            if (loaded) {
                return new String[0];
            }
            Printer.INSTANCE.prettyPrint("@EnableRedisShedLock 开启了RedisShedLock注解......");
            Printer.INSTANCE.prettyPrint("@EnableRedisShedLock 匹配的类: {}", annotationMetadata.getClassName());
            String[] imports = new String[]{RedisLockAutoConfiguration.class.getName()};
            loaded = true;
            return imports;
        }
    }
}
