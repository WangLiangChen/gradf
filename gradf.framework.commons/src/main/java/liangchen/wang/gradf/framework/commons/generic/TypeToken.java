package liangchen.wang.gradf.framework.commons.generic;

import liangchen.wang.gradf.framework.commons.exception.InfoException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author LiangChen.Wang
 * @date 18-12-18 上午11:05
 */
public abstract class TypeToken<T> {
    private final Type type;

    public TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new InfoException("No generics found!");
        }
        ParameterizedType type = (ParameterizedType) superclass;
        this.type = type.getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
}
