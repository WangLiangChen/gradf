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
        Arrays.stream(beanDefinitionNames).filter(e -> e.endsWith(OVERRIDE_SUFFIX)).forEach(overrideBeanDefinitionName -> {
            // 去除Override的beanDefinitionName
            String beanDefinitionName = overrideBeanDefinitionName.substring(0, overrideBeanDefinitionName.indexOf(OVERRIDE_SUFFIX));
            if (registry.containsBeanDefinition(beanDefinitionName)) {
                registry.removeBeanDefinition(beanDefinitionName);
            }
            BeanDefinition overrideBeanDefinition = registry.getBeanDefinition(overrideBeanDefinitionName);
            registry.removeBeanDefinition(overrideBeanDefinitionName);
            registry.registerBeanDefinition(beanDefinitionName, overrideBeanDefinition);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}
