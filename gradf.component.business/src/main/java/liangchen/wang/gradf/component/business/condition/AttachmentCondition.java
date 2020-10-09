package liangchen.wang.gradf.component.business.condition;


import liangchen.wang.gradf.component.business.enumeration.AttachmentStorageProvider;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import org.apache.commons.configuration2.Configuration;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @author LiangChen.Wang 2019/9/17 20:40
 */
public class AttachmentCondition implements Condition {
    private final Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration("attachment.properties");
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(AttachmentConditionAnnotation.class.getName());
        AttachmentStorageProvider value = (AttachmentStorageProvider)annotationAttributes.get("havingValue");
        String provider = configuration.getString("storage.provider");
        if(value.name().equals(provider)){
            return true;
        }
        return false;
    }
}
