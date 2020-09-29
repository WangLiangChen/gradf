package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.object.EnhancedObject;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;

import java.util.*;

/**
 * @author .LiangChen.Wang
 */
public class RoleAccountsInitialization extends FouraInitialization {
    private static final RoleAccountsInitialization self = new RoleAccountsInitialization();
    private Map<String, Set<String>> map = new HashMap<>();
    public static RoleAccountsInitialization newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public RoleAccountsInitialization put(String roleKey, String... loginNames) {
        Set<String> set = CollectionUtil.INSTANCE.array2Set(loginNames);
        put(roleKey, set);
        return this;
    }

    public RoleAccountsInitialization put(String roleKey, List<String> loginNames) {
        HashSet<String> set = new HashSet<>(loginNames);
        put(roleKey, set);
        return this;
    }

    public RoleAccountsInitialization put(String roleKey, Set<String> loginNames) {
        map.putIfAbsent(roleKey, loginNames);
        return this;
    }

    public Map<String, Set<String>> get() {
        return map;
    }
}
