package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.component.foura.dao.entity.Operation;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2020-04-12 23:47:02
 */
public class OperationParameterDomain extends ParameterDomain<Operation> {
    private static final OperationParameterDomain self = new OperationParameterDomain();
    private Long operation_id;
    private Long resource_id;
    private String operation_key;
    private String operation_text;
    private String depend_key;
    private Long operation_privilege;
    private String summary;

    public static OperationParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public Long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(Long operation_id) {
        this.operation_id = operation_id;
    }

    public Long getResource_id() {
        return resource_id;
    }

    public void setResource_id(Long resource_id) {
        this.resource_id = resource_id;
    }

    public String getOperation_key() {
        return operation_key;
    }

    public void setOperation_key(String operation_key) {
        this.operation_key = operation_key;
    }

    public String getOperation_text() {
        return operation_text;
    }

    public void setOperation_text(String operation_text) {
        this.operation_text = operation_text;
    }

    public String getDepend_key() {
        return depend_key;
    }

    public void setDepend_key(String depend_key) {
        this.depend_key = depend_key;
    }

    public Long getOperation_privilege() {
        return operation_privilege;
    }

    public void setOperation_privilege(Long operation_privilege) {
        this.operation_privilege = operation_privilege;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


}
