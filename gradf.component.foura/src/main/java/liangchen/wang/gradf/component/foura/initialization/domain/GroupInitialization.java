package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.digest.HashUtil;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import java.util.Objects;

/**
 * @author .LiangChen.Wang
 */
public class GroupInitialization extends FouraInitialization {
    private static final GroupInitialization self = new GroupInitialization();
    private String groupKey;
    private String groupText;

    public static GroupInitialization newInstance(String groupKey, String groupText) {
        GroupInitialization groupInitialization = ClassBeanUtil.INSTANCE.cast(self.clone());
        groupInitialization.groupKey = groupKey;
        groupInitialization.groupText = groupText;
        return groupInitialization;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public String getGroupText() {
        return groupText;
    }

    @Override
    public int hashCode() {
        return HashUtil.INSTANCE.hashCode(groupKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GroupInitialization)) {
            return false;
        }
        GroupInitialization other = (GroupInitialization) obj;
        return Objects.equals(this.groupKey, other.getGroupKey());
    }
}
