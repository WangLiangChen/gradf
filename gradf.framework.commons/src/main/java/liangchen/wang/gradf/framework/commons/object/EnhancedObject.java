package liangchen.wang.gradf.framework.commons.object;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.base.Splitter;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author LiangChen.Wang 2019/10/25 13:30
 */
public abstract class EnhancedObject implements Serializable, Cloneable {
    /**
     * 动态field，用于动态扩展类的filed
     */
    private Map<String, Object> dynamicFields = new HashMap<>();

    public int size() {
        return dynamicFields.size();
    }

    public boolean isEmpty() {
        return dynamicFields.isEmpty();
    }

    public boolean containsKey(String key) {
        return dynamicFields.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return dynamicFields.containsValue(value);
    }

    public Object get(String key) {
        return dynamicFields.get(key);
    }

    public Object put(String key, Object value) {
        return dynamicFields.put(key, value);
    }

    public void putAll(Map<String, Object> map) {
        dynamicFields.putAll(map);
    }

    public void clear() {
        dynamicFields.clear();
    }

    public Object remove(String key) {
        return dynamicFields.remove(key);
    }

    public Set<String> keySet() {
        return dynamicFields.keySet();
    }

    public Collection<Object> values() {
        return dynamicFields.values();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return dynamicFields.entrySet();
    }

    public Map<String, Object> getDynamicFields() {
        return dynamicFields;
    }

    public <T> T copyTo(Class<T> targetClass) {
        return ClassBeanUtil.INSTANCE.copyProperties(this, targetClass);
    }

    public <T> T copyTo(Class<T> targetClass, Consumer<T> consumer) {
        T t = ClassBeanUtil.INSTANCE.copyProperties(this, targetClass);
        if (null != consumer) {
            consumer.accept(t);
        }
        return t;
    }

    public void copyFrom(Object source) {
        ClassBeanUtil.INSTANCE.copyProperties(source, this);
    }

    protected MethodAccess methodAccess() {
        return MethodAccess.get(this.getClass());
    }

    protected List<String> methodNames() {
        MethodAccess methodAccess = methodAccess();
        String[] methodNameArray = methodAccess.getMethodNames();
        return Arrays.asList(methodNameArray);
    }

    public void initFields(String... ignoreFields) {
        Set<String> ignores = new HashSet<>();
        if (CollectionUtil.INSTANCE.isNotEmpty(ignoreFields)) {
            Arrays.stream(ignoreFields).forEach(s -> ignores.addAll(Splitter.on(',').splitToList(s)));
        }
        MethodAccess methodAccess = methodAccess();
        List<String> methodNames = methodNames();
        //noinspection rawtypes
        Class[] returnTypes = methodAccess.getReturnTypes();
        methodNames.forEach(setter -> {
            if (!setter.startsWith("set")) {
                return;
            }
            String fieldName = setter.substring(3);
            String getter = String.format("get%s", fieldName);
            if (!methodNames.contains(getter)) {
                return;
            }
            fieldName = StringUtil.INSTANCE.firstLetterConvertCase(fieldName);
            if (ignores.contains(fieldName)) {
                return;
            }
            int getterIndex = methodAccess.getIndex(getter);
            //noinspection rawtypes
            Class parameterType = returnTypes[getterIndex];
            initDefaultValue(methodAccess, getter, setter, parameterType);
        });
    }

    @SuppressWarnings("unchecked")
    private void initDefaultValue(MethodAccess methodAccess, String getter, String setter, @SuppressWarnings("rawtypes") Class parameterType) {
        Object invoke = methodAccess.invoke(this, getter);
        if (null != invoke) {
            return;
        }
        if (parameterType.isAssignableFrom(String.class)) {
            methodAccess.invoke(this, setter, "");
            return;
        }
        if (parameterType.isAssignableFrom(Byte.class)) {
            methodAccess.invoke(this, setter, (byte) 0);
            return;
        }
        if (parameterType.isAssignableFrom(Short.class)) {
            methodAccess.invoke(this, setter, (short) 0);
            return;
        }
        if (parameterType.isAssignableFrom(Integer.class)) {
            methodAccess.invoke(this, setter, 0);
            return;
        }
        if (parameterType.isAssignableFrom(Long.class)) {
            methodAccess.invoke(this, setter, 0L);
            return;
        }
        if (parameterType.isAssignableFrom(LocalDate.class)) {
            methodAccess.invoke(this, setter, LocalDate.now());
            return;
        }
        if (parameterType.isAssignableFrom(LocalDateTime.class)) {
            methodAccess.invoke(this, setter, LocalDateTime.now());
            return;
        }
        if (parameterType.isAssignableFrom(BigDecimal.class)) {
            methodAccess.invoke(this, setter, new BigDecimal(0));
        }
    }

    @Override
    public String toString() {
        return JsonUtil.INSTANCE.toJsonString(this);
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ErrorException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o != null && getClass() == o.getClass();
    }

    protected boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    protected int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    protected int deepHashCode(Object... objects) {
        return Arrays.deepHashCode(objects);
    }

    protected int hashCodePositive(Object... objects) {
        return Arrays.hashCode(objects) & Integer.MAX_VALUE;
    }

    protected boolean deepEquals(Object a, Object b) {
        return Objects.deepEquals(a, b);
    }
}
