package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.object.EnhancedObject;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;

import java.util.*;

/**
 * @author .LiangChen.Wang
 */
public class GroupAccountsInitialization extends FouraInitialization {
    private static final GroupAccountsInitialization self = new GroupAccountsInitialization();
    private Map<String, Set<String>> map = new HashMap<>();

    public static GroupAccountsInitialization newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public GroupAccountsInitialization put(String groupKey, String... loginNames) {
        Set<String> set = CollectionUtil.INSTANCE.array2Set(loginNames);
        put(groupKey, set);
        return this;
    }

    public GroupAccountsInitialization put(String groupKey, List<String> loginNames) {
        HashSet<String> set = new HashSet<>(loginNames);
        put(groupKey, set);
        return this;
    }

    public void put(String groupKey, Set<String> loginNames) {
        map.putIfAbsent(groupKey, loginNames);
    }

    public Map<String, Set<String>> get() {
        return map;
    }

}
