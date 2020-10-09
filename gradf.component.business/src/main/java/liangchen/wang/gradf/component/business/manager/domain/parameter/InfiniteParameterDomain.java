package liangchen.wang.gradf.component.business.manager.domain.parameter;

import liangchen.wang.gradf.component.business.dao.entity.Infinite;
import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2020-05-12 19:37:27
 */
public class InfiniteParameterDomain extends ParameterDomain<Infinite> {
    private static final InfiniteParameterDomain self = new InfiniteParameterDomain();

    public static InfiniteParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long infinite_id;
    private Long parent_id;
    private String infinite_value;
    private String infinite_text;
    private String infinite_flag;
    private String business_flag;
    private String summary;
    private String status;

    public Long getInfinite_id() {
        return infinite_id;
    }

    public void setInfinite_id(Long infinite_id) {
        this.infinite_id = infinite_id;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public String getInfinite_value() {
        return infinite_value;
    }

    public void setInfinite_value(String infinite_value) {
        this.infinite_value = infinite_value;
    }

    public String getInfinite_text() {
        return infinite_text;
    }

    public void setInfinite_text(String infinite_text) {
        this.infinite_text = infinite_text;
    }

    public String getInfinite_flag() {
        return infinite_flag;
    }

    public void setInfinite_flag(String infinite_flag) {
        this.infinite_flag = infinite_flag;
    }

    public String getBusiness_flag() {
        return business_flag;
    }

    public void setBusiness_flag(String business_flag) {
        this.business_flag = business_flag;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
