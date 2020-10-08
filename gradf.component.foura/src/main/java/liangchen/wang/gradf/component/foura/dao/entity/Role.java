package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Entity(name = "gradf_role")
public class Role extends BaseEntity {
    private static final Role self = new Role();
    @Id
    private Long role_id;
    private Long parent_id;
    private String role_key;
    private String role_text;

    public static Role newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public String getRole_key() {
        return role_key;
    }

    public void setRole_key(String role_key) {
        this.role_key = role_key;
    }

    public String getRole_text() {
        return role_text;
    }

    public void setRole_text(String role_text) {
        this.role_text = role_text;
    }

}
