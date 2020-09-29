package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.object.EnhancedObject;
import liangchen.wang.gradf.component.foura.dao.entity.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * @author .LiangChen.Wang
 */
public class GroupInitialization extends FouraInitialization {
    private static final GroupInitialization self = new GroupInitialization();

    public static GroupInitialization newInstance(String groupKey,String groupText) {
        GroupInitialization groupInitialization = ClassBeanUtil.INSTANCE.cast(self.clone());
        groupInitialization.groupKey = groupKey;
        groupInitialization.groupText = groupText;
        return groupInitialization;
    }


    private String groupKey;
    private String groupText;

    public String getGroupKey() {
        return groupKey;
    }

    public String getGroupText() {
        return groupText;
    }
}
