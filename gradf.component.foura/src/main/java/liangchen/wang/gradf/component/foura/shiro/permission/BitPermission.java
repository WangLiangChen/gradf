package liangchen.wang.gradf.component.foura.shiro.permission;

import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import org.apache.shiro.authz.Permission;

/**
 * 规则 资源字符串+权限位+实例ID
 * <p>
 * 中间通过+分割
 * <p>
 * 如 user+10 表示对资源user拥有修改/查看权限
 *
 * @author LiangChen.Wang
 */
public class BitPermission implements Permission {

    private String resource;
    private int privilege;
    private String instanceId;

    BitPermission(String permissionString) {
        String[] array = permissionString.split("\\+");

        if (array.length > 1) {
            resource = array[1];
        }

        if (StringUtil.INSTANCE.isBlank(resource)) {
            resource = Symbol.STAR.getSymbol();
        }

        if (array.length > 2) {
            privilege = Integer.valueOf(array[2]);
        }

        if (array.length > 3) {
            instanceId = array[3];
        }

        if (StringUtil.INSTANCE.isBlank(instanceId)) {
            instanceId = Symbol.STAR.getSymbol();
        }

    }

    @Override
    public boolean implies(Permission permission) {
        if (!(permission instanceof BitPermission)) {
            return false;
        }
        BitPermission bitPermission = (BitPermission) permission;

        if (!(Symbol.STAR.getSymbol().equals(this.resource) || this.resource.equals(bitPermission.resource))) {
            return false;
        }

        if (!(this.privilege == 0 || (bitPermission.privilege & this.privilege) == bitPermission.privilege)) {
            return false;
        }

        return Symbol.STAR.getSymbol().equals(this.instanceId) || this.instanceId.equals(bitPermission.instanceId);
    }

    @Override
    public String toString() {
        return "BitPermission{" + "resource='" + resource + '\'' + ", privilege=" + privilege + ", instanceId='" + instanceId + '\'' + '}';
    }

}
