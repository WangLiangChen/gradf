package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.component.foura.dao.entity.OperationLog;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2020-03-07 10:24:36
 */
public class OperationLogParameterDomain extends ParameterDomain<OperationLog> {
    private static final OperationLogParameterDomain self = new OperationLogParameterDomain();

    public static OperationLogParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long log_id;
    @NotNull(message = "业务ID不能为空")
    private Long business_id;
    @NotBlank(message = "业务类型不能为空")
    private String business_type;
    @NotBlank(message = "业务名称不能为空")
    private String business_name;
    private Long operator_id;
    private String operator_name;
    @NotBlank(message = "操作标识不能为空")
    private String operation_flag;
    @NotBlank(message = "操作标识名称不能为空")
    private String operation_name;
    private String source_class;
    private String source_method;
    private String original_data;
    private String new_data;
    private String operation_parameter;
    private String operation_return;
    private Byte operation_success;
    private java.time.LocalDateTime create_datetime;
    private String summary;
    private String status;

    public Long getLog_id() {
        return log_id;
    }

    public void setLog_id(Long log_id) {
        this.log_id = log_id;
    }

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public Long getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(Long operator_id) {
        this.operator_id = operator_id;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getOperation_flag() {
        return operation_flag;
    }

    public void setOperation_flag(String operation_flag) {
        this.operation_flag = operation_flag;
    }

    public String getOperation_name() {
        return operation_name;
    }

    public void setOperation_name(String operation_name) {
        this.operation_name = operation_name;
    }

    public String getSource_class() {
        return source_class;
    }

    public void setSource_class(String source_class) {
        this.source_class = source_class;
    }

    public String getSource_method() {
        return source_method;
    }

    public void setSource_method(String source_method) {
        this.source_method = source_method;
    }

    public String getOriginal_data() {
        return original_data;
    }

    public void setOriginal_data(String original_data) {
        this.original_data = original_data;
    }

    public String getNew_data() {
        return new_data;
    }

    public void setNew_data(String new_data) {
        this.new_data = new_data;
    }

    public String getOperation_parameter() {
        return operation_parameter;
    }

    public void setOperation_parameter(String operation_parameter) {
        this.operation_parameter = operation_parameter;
    }

    public String getOperation_return() {
        return operation_return;
    }

    public void setOperation_return(String operation_return) {
        this.operation_return = operation_return;
    }

    public Byte getOperation_success() {
        return operation_success;
    }

    public void setOperation_success(Byte operation_success) {
        this.operation_success = operation_success;
    }

    public java.time.LocalDateTime getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(java.time.LocalDateTime create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
