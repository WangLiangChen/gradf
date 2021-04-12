package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.object.EnhancedObject;

/**
 * @author LiangChen.Wang 2021/4/12
 */
public class TestBean extends EnhancedObject {
    private Long beanId;
    private String beanName;

    public Long getBeanId() {
        return beanId;
    }

    public void setBeanId(Long beanId) {
        this.beanId = beanId;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
