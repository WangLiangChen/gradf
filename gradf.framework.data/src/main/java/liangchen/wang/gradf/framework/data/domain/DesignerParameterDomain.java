package liangchen.wang.gradf.framework.data.domain;

import liangchen.wang.crdf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.crdf.framework.commons.object.EnhancedObject;

import javax.validation.constraints.NotBlank;

/**
 * @author LiangChen.Wang 2019/11/22 16:05
 */
public class DesignerParameterDomain extends EnhancedObject {
    private static final DesignerParameterDomain self = new DesignerParameterDomain();

    public static DesignerParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.classCast(self.clone());
    }

    @NotBlank(message = "模块名不能为空")
    private String moduleName;
    @NotBlank(message = "业务名不能为空")
    private String businessName;
    @NotBlank(message = "业务标识不能为空")
    private String businessType;
    @NotBlank(message = "数据表名不能为空")
    private String tableName;
    @NotBlank(message = "数据实体不能为空")
    private String entityName;
    @NotBlank(message = "导出目录不能为空")
    private String exportPath;
    @NotBlank(message = "根包名不能为空")
    private String mainPackage;
    @NotBlank(message = "作者/开发者不能为空")
    private String author;
    private boolean extendedBaseEntity;
    private String statusFieldClassName;
    private String statusField;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    public String getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(String mainPackage) {
        this.mainPackage = mainPackage;
    }

    public boolean isExtendedBaseEntity() {
        return extendedBaseEntity;
    }

    public void setExtendedBaseEntity(boolean extendedBaseEntity) {
        this.extendedBaseEntity = extendedBaseEntity;
    }

    public String getStatusFieldClassName() {
        return statusFieldClassName;
    }

    public void setStatusFieldClassName(String statusFieldClassName) {
        this.statusFieldClassName = statusFieldClassName;
    }

    public String getStatusField() {
        return statusField;
    }

    public void setStatusField(String statusField) {
        this.statusField = statusField;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
