package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.base.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-04-12 01:44:04
 */
@Table(name = "crdf_group")
public class GroupQuery extends RootQuery {
    private static final GroupQuery self = new GroupQuery();

    public static GroupQuery newInstance() {
        return ClassBeanUtil.INSTANCE.classCast(self.clone());
    }

    public static GroupQuery newInstance(Long group_id) {
        GroupQuery query = newInstance();
        query.setGroup_id(group_id);
        return query;
    }

    @Query(operator = Operator.EQUALS, column = "group_id")
    private Long group_id;
    @Query(operator = Operator.EQUALS, column = "group_key")
    private String group_key;

    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getStatusIn() {
        return statusIn;
    }

    public void setStatusIn(String[] statusIn) {
        this.statusIn = statusIn;
    }

    public String[] getStatusNotIn() {
        return statusNotIn;
    }

    public void setStatusNotIn(String[] statusNotIn) {
        this.statusNotIn = statusNotIn;
    }

    public void setGroup_key(String group_key) {
        this.group_key = group_key;
    }

    public String getGroup_key() {
        return group_key;
    }
}
