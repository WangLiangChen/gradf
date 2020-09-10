package liangchen.wang.gradf.framework.commons.object;

import com.esotericsoftware.reflectasm.MethodAccess;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.json.JSONUtil;
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

    public boolean containsKey(Object key) {
        boolean result = dynamicFields.containsKey(key);
        if ((!result) && key instanceof Number) {
            result = dynamicFields.containsKey(key.toString());
        }
        return result;
    }

    public boolean containsValue(Object value) {
        return dynamicFields.containsValue(value);
    }

    public Object get(Object key) {
        Object val = dynamicFields.get(key);
        if (val == null && key instanceof Number) {
            val = dynamicFields.get(key.toString());
        }
        return val;
    }

    public Object put(String key, Object value) {
        return dynamicFields.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        dynamicFields.putAll(m);
    }

    public void clear() {
        dynamicFields.clear();
    }

    public Object remove(Object key) {
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
        List<String> methodNames = Arrays.asList(methodNameArray);
        return methodNames;
    }

    public void initFields(String... ignoreFields) {
        Set<String> ignores = new HashSet<>();
        if (CollectionUtil.INSTANCE.isNotEmpty(ignoreFields)) {
            Arrays.stream(ignoreFields).forEach(s -> {
                String[] split = s.split(",");
                Arrays.stream(split).forEach(ignores::add);
            });
        }
        MethodAccess methodAccess = methodAccess();
        List<String> methodNames = methodNames();
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
            Class parameterType = returnTypes[getterIndex];
            initDefaultValue(methodAccess, getter, setter, parameterType);
        });
    }

    @SuppressWarnings("unchecked")
    private void initDefaultValue(MethodAccess methodAccess, String getter, String setter, Class parameterType) {
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
            return;
        }
    }

    @Override
    public String toString() {
        return JSONUtil.INSTANCE.toJSONString(this);
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ErrorException(e);
        }
    }
}
