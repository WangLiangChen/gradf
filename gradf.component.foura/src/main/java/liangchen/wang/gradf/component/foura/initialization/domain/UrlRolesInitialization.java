package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;

import java.util.*;

/**
 * @author .LiangChen.Wang
 */
public class UrlRolesInitialization extends FouraInitialization {
    private static final UrlRolesInitialization self = new UrlRolesInitialization();
    private Map<String, Set<String>> map = new HashMap<>();

    public static UrlRolesInitialization newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public UrlRolesInitialization put(String urlPath, String... roleKeys) {
        Set<String> set = CollectionUtil.INSTANCE.array2Set(roleKeys);
        put(urlPath, set);
        return this;
    }

    public UrlRolesInitialization put(String urlPath, List<String> roleKeys) {
        HashSet<String> set = new HashSet<>(roleKeys);
        put(urlPath, set);
        return this;
    }

    public UrlRolesInitialization put(String urlPath, Set<String> roleKeys) {
        map.putIfAbsent(urlPath, roleKeys);
        return this;
    }

    public Map<String, Set<String>> get() {
        return map;
    }

}
