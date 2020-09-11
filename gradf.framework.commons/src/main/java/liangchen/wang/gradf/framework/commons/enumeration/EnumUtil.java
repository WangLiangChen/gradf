package liangchen.wang.gradf.framework.commons.enumeration;


import liangchen.wang.gradf.framework.commons.exception.ErrorException;

/**
 * @author LiangChen.Wang
 */
public class EnumUtil {
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        try {
            return Enum.valueOf(enumType, name);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ErrorException("Enum转换错误:" + e.getMessage());
        }
    }
}
