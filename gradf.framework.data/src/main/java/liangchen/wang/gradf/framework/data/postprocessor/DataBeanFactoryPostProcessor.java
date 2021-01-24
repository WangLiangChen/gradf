package liangchen.wang.gradf.framework.data.postprocessor;

import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author LiangChen.Wang
 */
@Component
public class DataBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] abstractDao = configurableListableBeanFactory.getBeanNamesForType(AbstractJdbcDao.class);
        if (CollectionUtil.INSTANCE.isEmpty(abstractDao)) {
            return;
        }
        boolean hasDataSource = configurableListableBeanFactory.containsBean("dataSource");
        if (hasDataSource) {
            return;
        }
        throw new InfoException("Please use annotation '@EnableJdbc' to enable JDBC");
    }
}
