package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;

import java.util.*;

/**
 * @author .LiangChen.Wang
 */
public class GroupRolesInitialization extends FouraInitialization {
    private static final GroupRolesInitialization self = new GroupRolesInitialization();
    private Map<String, Set<String>> map = new HashMap<>();

    public static GroupRolesInitialization newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public GroupRolesInitialization put(String groupKey, String... roleKeys) {
        Set<String> set = CollectionUtil.INSTANCE.array2Set(roleKeys);
        put(groupKey, set);
        return this;
    }

    public GroupRolesInitialization put(String groupKey, List<String> roleKeys) {
        HashSet<String> set = new HashSet<>(roleKeys);
        put(groupKey, set);
        return this;
    }

    public void put(String groupKey, Set<String> roleKeys) {
        map.putIfAbsent(groupKey, roleKeys);
    }

    public Map<String, Set<String>> get() {
        return map;
    }

}
