package liangchen.wang.gradf.component.foura.test;

import liangchen.wang.gradf.framework.data.annotation.EnableJdbc;
import liangchen.wang.gradf.framework.data.domain.DesignerParameterDomain;
import liangchen.wang.gradf.framework.data.manager.IDesignerManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

/**
 * @author LiangChen.Wang 2019/11/12 14:41
 */
@SpringBootTest
@EnableJdbc
public class DesignerTest {
    @Inject
    private IDesignerManager manager;

    @Test
    public void createGroupRole() {
        String businessType = "GroupRole";
        String businessName = "群组角色";
        String tableName = "gradf_group_role";
        String entityName = "GroupRole";
        String moduleName = "Gradf_Foura";
        String exportPath = "E:\\AutoCode";
        String mainPackage = "liangchen.wang.gradf.component.foura";
        String author = "LiangChen.Wang";
        DesignerParameterDomain parameter = DesignerParameterDomain.newInstance();
        parameter.setBusinessName(businessName);
        parameter.setBusinessType(businessType);
        parameter.setTableName(tableName);
        parameter.setEntityName(entityName);
        parameter.setModuleName(moduleName);
        parameter.setExportPath(exportPath);
        parameter.setMainPackage(mainPackage);
        parameter.setStatusField("status");
        parameter.setStatusFieldClassName("String");
        parameter.setAuthor(author);
        manager.columns(parameter);
    }
}
