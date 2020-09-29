package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.object.EnhancedObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author .LiangChen.Wang
 */
public class RoleInitialization extends FouraInitialization {
    private static final RoleInitialization self = new RoleInitialization();

    public static RoleInitialization newInstance(String roleKey, String roleText) {
        RoleInitialization roleInitialization = ClassBeanUtil.INSTANCE.cast(self.clone());
        roleInitialization.roleKey = roleKey;
        roleInitialization.roleText = roleText;
        return roleInitialization;
    }


    private String roleKey;
    private String roleText;

    public String getRoleKey() {
        return roleKey;
    }

    public String getRoleText() {
        return roleText;
    }

}
