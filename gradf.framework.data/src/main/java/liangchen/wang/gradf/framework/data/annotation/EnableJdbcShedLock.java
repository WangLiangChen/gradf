package liangchen.wang.gradf.framework.data.annotation;


import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.data.configuration.JdbcShedLockProviderAutoConfiguration;
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
@Import({EnableJdbcShedLock.ShedLockImportSelector.class})
public @interface EnableJdbcShedLock {
    class ShedLockImportSelector implements ImportSelector {
        private static boolean loaded = false;

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            if (loaded) {
                return new String[0];
            }
            Printer.INSTANCE.prettyPrint("开启了JdbcShedLock注解......");
            String[] imports = new String[]{JdbcShedLockProviderAutoConfiguration.class.getName()};
            loaded = true;
            return imports;
        }
    }
}
