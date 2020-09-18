package liangchen.wang.gradf.framework.data.entity;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2019/11/13 17:51
 */
public class Between implements Cloneable {
    private static final Between self = new Between();

    public static Between newInstance(Object min, Object max) {
        try {
            Between between = ClassBeanUtil.INSTANCE.cast(self.clone());
            between.min = min;
            between.max = max;
            return between;
        } catch (CloneNotSupportedException e) {
            throw new ErrorException(e);
        }
    }

    private Object min;
    private Object max;

    public Object getMin() {
        return min;
    }

    public Object getMax() {
        return max;
    }

}