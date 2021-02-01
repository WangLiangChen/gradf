package liangchen.wang.gradf.framework.data.utils;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.core.RootEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiangChen.Wang at 2021/2/1 8:26
 * 设计模式：原型模式，缓存并clone对象
 */
public enum EntityPrototypeUtil {
    INSTANCE;
    private Map<String, RootEntity> entities = new HashMap<>();

    public <T> T clone(Class<T> clazz) {
        Assert.INSTANCE.notNull(clazz, "Parameter 'clazz' cannot be null");
        return clone(clazz.getName());
    }

    public <T> T clone(String className) {
        Assert.INSTANCE.notBlank(className, "Parameter 'className' cannot be null or blank");
        RootEntity rootEntity = loadPrototype(className);
        return ClassBeanUtil.INSTANCE.cast(rootEntity.clone());
    }

    private RootEntity loadPrototype(String className) {
        RootEntity rootEntity = entities.get(className);
        if (null != rootEntity) {
            return rootEntity;
        }
        synchronized (className.intern()) {
            rootEntity = entities.get(className);
            if (null != rootEntity) {
                return rootEntity;
            }
            rootEntity = createInstance(className);
            entities.put(className, rootEntity);
            return rootEntity;
        }
    }

    private RootEntity createInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor();
            return (RootEntity) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ErrorException(e);
        }
    }

}
