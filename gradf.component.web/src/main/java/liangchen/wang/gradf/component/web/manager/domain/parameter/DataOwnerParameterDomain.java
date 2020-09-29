package liangchen.wang.gradf.component.web.manager.domain.parameter;

import liangchen.wang.gradf.component.web.base.ParameterDomain;
import liangchen.wang.gradf.component.web.dao.entity.DataOwner;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2019-12-29 20:29:37
*/
public class DataOwnerParameterDomain extends ParameterDomain<DataOwner> {
    private static final DataOwnerParameterDomain self = new DataOwnerParameterDomain();

    public static DataOwnerParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long record_id;
    private Long data_id;
    private String data_type;
    private Long owner_id;
    private String owner_type;

    public Long getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Long record_id) {
        this.record_id = record_id;
    }

    public Long getData_id() {
        return data_id;
    }

    public void setData_id(Long data_id) {
        this.data_id = data_id;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public Long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Long owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(String owner_type) {
        this.owner_type = owner_type;
    }



}
