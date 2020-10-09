package liangchen.wang.gradf.component.business.manager.domain.result;

import liangchen.wang.gradf.component.commons.base.ResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2020-05-12 19:37:27
*/
public class InfiniteResultDomain extends ResultDomain {
    private static final InfiniteResultDomain self = new InfiniteResultDomain();

    public static InfiniteResultDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long infinite_id;
    private Long parent_id;
    private String infinite_value;
    private String infinite_text;
    private Byte infinite_grade;
    private Short infinite_left;
    private Short infinite_right;
    private String infinite_flag;
    private String business_flag;
    private Integer sort;
    private java.time.LocalDateTime create_datetime;
    private java.time.LocalDateTime modify_datetime;
    private Long creator;
    private Long modifier;
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

    public Byte getInfinite_grade() {
        return infinite_grade;
    }

    public void setInfinite_grade(Byte infinite_grade) {
        this.infinite_grade = infinite_grade;
    }

    public Short getInfinite_left() {
        return infinite_left;
    }

    public void setInfinite_left(Short infinite_left) {
        this.infinite_left = infinite_left;
    }

    public Short getInfinite_right() {
        return infinite_right;
    }

    public void setInfinite_right(Short infinite_right) {
        this.infinite_right = infinite_right;
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


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public java.time.LocalDateTime getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(java.time.LocalDateTime create_datetime) {
        this.create_datetime = create_datetime;
    }

    public java.time.LocalDateTime getModify_datetime() {
        return modify_datetime;
    }

    public void setModify_datetime(java.time.LocalDateTime modify_datetime) {
        this.modify_datetime = modify_datetime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
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
