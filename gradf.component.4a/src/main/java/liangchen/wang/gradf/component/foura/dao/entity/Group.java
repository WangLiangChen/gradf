package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-04-12 01:44:04
*/
@Entity(name = "gradf_group")
public class Group extends BaseEntity {
    private static final Group self = new Group();

    public static Group newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Id
    private Long group_id;
    private Long parent_id;
    private String group_key;
    private String group_text;

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public String getGroup_key() {
        return group_key;
    }

    public void setGroup_key(String group_key) {
        this.group_key = group_key;
    }

    public String getGroup_text() {
        return group_text;
    }

    public void setGroup_text(String group_text) {
        this.group_text = group_text;
    }

}
