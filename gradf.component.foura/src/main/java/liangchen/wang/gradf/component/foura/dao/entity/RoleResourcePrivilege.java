package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:23
 */
@Entity(name = "gradf_role_resource_privilege")
public class RoleResourcePrivilege extends BaseEntity {
    private static final RoleResourcePrivilege self = new RoleResourcePrivilege();
    @Id
    private Long role_id;
    @Id
    private Long resource_id;
    private Long privilege;


    public static RoleResourcePrivilege newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getResource_id() {
        return resource_id;
    }

    public void setResource_id(Long resource_id) {
        this.resource_id = resource_id;
    }

    public Long getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Long privilege) {
        this.privilege = privilege;
    }


}
