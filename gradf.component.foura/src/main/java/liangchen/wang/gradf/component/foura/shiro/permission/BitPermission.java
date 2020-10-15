package liangchen.wang.gradf.component.foura.shiro.permission;

import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import org.apache.shiro.authz.Permission;

/**
 * 格式：资源字符串+权限位+实例ID，使用+分割
 * <p>
 * 如 user+10 表示对资源user拥有修改/查看权限
 *
 * @author LiangChen.Wang
 */
public class BitPermission implements Permission {

    private String resource;
    private int privilege;
    private String instanceId;

    BitPermission(String permission) {
        String[] array = permission.split("\\+");
        switch (array.length) {
            case 1:
                resource = array[0];
                break;
            case 2:
                resource = array[0];
                privilege = Integer.parseInt(array[1]);
                break;
            case 3:
                resource = array[0];
                privilege = Integer.parseInt(array[1]);
                instanceId = array[2];
                break;
            default:
                throw new InfoException("permission string format error");
        }

        if (StringUtil.INSTANCE.isBlank(resource)) {
            resource = Symbol.STAR.getSymbol();
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
        BitPermission otherPermission = (BitPermission) permission;
        // 验证resource
        if (!(Symbol.STAR.getSymbol().equals(this.resource) || this.resource.equals(otherPermission.resource))) {
            return false;
        }
        // 验证privilege
        if (!(this.privilege == 0 || (otherPermission.privilege & this.privilege) == otherPermission.privilege)) {
            return false;
        }
        return Symbol.STAR.getSymbol().equals(this.instanceId) || this.instanceId.equals(otherPermission.instanceId);
    }

    @Override
    public String toString() {
        return "BitPermission{" + "resource='" + resource + '\'' + ", privilege=" + privilege + ", instanceId='" + instanceId + '\'' + '}';
    }

}
