package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.digest.HashUtil;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import java.util.Objects;

/**
 * @author .LiangChen.Wang
 */
public class RoleInitialization extends FouraInitialization {
    private static final RoleInitialization self = new RoleInitialization();
    private String roleKey;
    private String roleText;

    public static RoleInitialization newInstance(String roleKey, String roleText) {
        RoleInitialization roleInitialization = ClassBeanUtil.INSTANCE.cast(self.clone());
        roleInitialization.roleKey = roleKey;
        roleInitialization.roleText = roleText;
        return roleInitialization;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public String getRoleText() {
        return roleText;
    }

    @Override
    public int hashCode() {
        return HashUtil.INSTANCE.hashCode(roleKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RoleInitialization)) {
            return false;
        }
        RoleInitialization other = (RoleInitialization) obj;
        return Objects.equals(this.roleKey, other.getRoleKey());
    }
}
