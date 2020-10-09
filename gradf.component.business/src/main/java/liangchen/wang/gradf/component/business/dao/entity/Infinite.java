package liangchen.wang.gradf.component.business.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-05-12 19:37:26
*/
@Entity(name = "gradf_infinite")
public class Infinite extends BaseEntity {
    private static final Infinite self = new Infinite();

    public static Infinite newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Id
    private Long infinite_id;
    private Long parent_id;
    private String infinite_value;
    private String infinite_text;
    private Byte infinite_grade;
    private Short infinite_left;
    private Short infinite_right;
    private String infinite_flag;
    private String business_flag;

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

}
