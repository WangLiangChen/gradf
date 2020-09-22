package liangchen.wang.gradf.framework.cache.primary;

import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * @author LiangChen.Wang
 */
public class DefaultKeyGenerator implements KeyGenerator {
    public static final String NO_PARAM_KEY = "-NO_PARAM";
    public static final String NULL_PARAM_KEY = "-NULL_PARAM";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(method.getName());
        if (params.length == 0) {
            return key.append(NO_PARAM_KEY).toString();
        }
        for (Object param : params) {
            key.append('-');
            if (null == param) {
                key.append(NULL_PARAM_KEY);
            } else {
                key.append(JsonUtil.INSTANCE.toJsonStringWithTransientField(param));
            }
        }
        return key.toString();
    }
}
