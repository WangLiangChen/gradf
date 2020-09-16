package liangchen.wang.gradf.framework.web.annotation;

import liangchen.wang.gradf.framework.web.configuration.WebFluxAutoConfiguration;
import liangchen.wang.gradf.framework.web.configuration.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EnableWeb.WebImportSelector.class})
public @interface EnableWeb {
    WebType webType() default WebType.WEBMVC;

    enum WebType {
        /**
         *
         */
        WEBFLUX, WEBMVC;
    }

    class WebImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            Class<?> annotationType = EnableWeb.class;
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(annotationType.getName(), false));
            EnableWeb.WebType webType = (EnableWeb.WebType) attributes.get("webType");
            String[] imports = null;
            switch (webType) {
                case WEBMVC:
                    imports = new String[]{DelegatingWebMvcConfiguration.class.getName(), WebMvcAutoConfiguration.class.getName()};
                    break;
                case WEBFLUX:
                    imports = new String[]{DelegatingWebFluxConfiguration.class.getName(), WebFluxAutoConfiguration.class.getName()};
                    break;
                default:
                    imports = new String[0];
                    break;
            }
            return imports;
        }
    }
}
