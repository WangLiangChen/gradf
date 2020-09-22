package liangchen.wang.gradf.framework.cache.annotation;

import liangchen.wang.gradf.framework.cache.aspect.GradfCachingAspect;
import liangchen.wang.gradf.framework.cache.caffeine.GradfCaffeineCacheManager;
import liangchen.wang.gradf.framework.commons.utils.Printer;
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
@Import({EnableGradfCaching.CachingImportSelector.class})
public @interface EnableGradfCaching {
    class CachingImportSelector implements ImportSelector {
        private static boolean loaded = false;

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            if (loaded) {
                return new String[0];
            }
            Printer.INSTANCE.prettyPrint("开启了缓存注解......");
            String[] imports = new String[]{GradfCaffeineCacheManager.class.getName(), GradfCachingAspect.class.getName()};
            loaded = true;
            return imports;
        }
    }
}
