package liangchen.wang.gradf.framework.springboot.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 用于实现Bean被覆盖的效果
 * 比如将以xxxOverride命名的BeanDefinition重新注册名为xxx
 *
 * @author LiangChen.Wang 2020/9/15
 */
@Component
public class OverrideBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private final String OVERRIDE_SUFFIX = "Override";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).filter(e -> e.endsWith(OVERRIDE_SUFFIX)).forEach(beanDefinitionName -> {
            String overriddenBeanDefinitionName = beanDefinitionName.substring(0, beanDefinitionName.indexOf(OVERRIDE_SUFFIX));
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            registry.removeBeanDefinition(overriddenBeanDefinitionName);
            registry.removeBeanDefinition(beanDefinitionName);
            registry.registerBeanDefinition(overriddenBeanDefinitionName, beanDefinition);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}
