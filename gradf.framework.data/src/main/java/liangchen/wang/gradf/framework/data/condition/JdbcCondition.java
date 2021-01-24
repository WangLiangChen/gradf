package liangchen.wang.gradf.framework.data.condition;

import liangchen.wang.gradf.framework.data.enumeration.DataStatus;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author LiangChen.Wang
 */
public class JdbcCondition implements ConfigurationCondition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return DataStatus.INSTANCE.isJdbcEnable();
    }

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        /*强制在注册阶段判断条件，而不是在解析阶段,解决条件判断在Import之前的问题.
         * 解析阶段--shouldSkip---Import--注册阶段--shouldSkip
         * */
        return ConfigurationPhase.REGISTER_BEAN;
    }
}
