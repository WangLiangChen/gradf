package liangchen.wang.gradf.framework.commons.json;

/**
 * @author WangLiangChen
 */
public abstract class Json {
    public static String toJSONString(Object object) {
        return JsonUtil.INSTANCE.toJsonString(object);
    }
}
