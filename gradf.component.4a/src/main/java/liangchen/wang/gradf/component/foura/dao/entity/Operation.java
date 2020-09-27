package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.base.RootEntity;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang 2020-04-12 23:47:02
 */
@Entity(name = "crdf_operation")
public class Operation extends RootEntity {
    private static final Operation self = new Operation();

    public static Operation newInstance() {
        return ClassBeanUtil.INSTANCE.classCast(self.clone());
    }

    @Id
    private Long operation_id;
    private Long resource_id;
    private String operation_key;
    private String operation_text;
    private String depend_key;
    private Long operation_privilege;
    private LocalDateTime create_datetime;
    private String summary;

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

    public LocalDateTime getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(LocalDateTime create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
