package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Entity(name = "gradf_group_account")
public class GroupAccount extends BaseEntity {
    private static final GroupAccount self = new GroupAccount();
    @Id
    private Long group_id;
    @Id
    private Long account_id;

    public static GroupAccount newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

}
