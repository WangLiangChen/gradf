package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-10-08 19:11:17
*/
@Entity(name = "gradf_group_role")
public class GroupRole extends BaseEntity {
    private static final GroupRole self = new GroupRole();

    public static GroupRole newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Id
    private Long group_id;
    @Id
    private Long role_id;

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

}
