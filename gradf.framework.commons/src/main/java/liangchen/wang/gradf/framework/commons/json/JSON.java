package liangchen.wang.gradf.framework.commons.json;

public abstract class JSON {
    public static String toJSONString(Object object) {
        return JSONUtil.INSTANCE.toJSONString(object);
    }
}
