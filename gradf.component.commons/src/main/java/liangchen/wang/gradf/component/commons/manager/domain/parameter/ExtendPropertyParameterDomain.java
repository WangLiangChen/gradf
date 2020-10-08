package liangchen.wang.gradf.component.commons.manager.domain.parameter;


import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.component.commons.dao.entity.ExtendProperty;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2019-12-29 20:32:33
 */
public class ExtendPropertyParameterDomain extends ParameterDomain<ExtendProperty> {
    private static final ExtendPropertyParameterDomain self = new ExtendPropertyParameterDomain();

    public static ExtendPropertyParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long property_id;
    private String property_key;
    private String property_text;
    private String property_value;
    private String data_type;
    private String property_summary;
    private Long business_id;
    private String business_type;

    public Long getProperty_id() {
        return property_id;
    }

    public void setProperty_id(Long property_id) {
        this.property_id = property_id;
    }

    public String getProperty_key() {
        return property_key;
    }

    public void setProperty_key(String property_key) {
        this.property_key = property_key;
    }

    public String getProperty_text() {
        return property_text;
    }

    public void setProperty_text(String property_text) {
        this.property_text = property_text;
    }

    public String getProperty_value() {
        return property_value;
    }

    public void setProperty_value(String property_value) {
        this.property_value = property_value;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getProperty_summary() {
        return property_summary;
    }

    public void setProperty_summary(String property_summary) {
        this.property_summary = property_summary;
    }

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

}
