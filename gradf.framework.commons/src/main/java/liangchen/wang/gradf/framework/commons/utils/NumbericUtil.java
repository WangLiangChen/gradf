package liangchen.wang.gradf.framework.commons.utils;

/**
 * @author LiangChen.Wang 2019/8/30 16:59
 */
public enum NumbericUtil {
    /**
     *
     */
    INSTANCE;

    public boolean isNull(Number number) {
        return null == number;
    }

    public boolean isNotNull(Number number) {
        return null != number;
    }

    public boolean isNullOrZero(Number number) {
        return null == number || number.equals(0);
    }

    public boolean isNotNullAndZero(Number number) {
        return null != number & !number.equals(0);
    }

    public boolean isZero(Number number) {
        if (null == number) {
            return false;
        }
        return number.equals(0);
    }

    public boolean isNotZero(Number number) {
        if (null == number) {
            return true;
        }
        return !number.equals(0);
    }
}
