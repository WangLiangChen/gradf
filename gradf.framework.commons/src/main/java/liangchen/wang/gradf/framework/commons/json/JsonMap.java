package liangchen.wang.gradf.framework.commons.json;

import java.io.Serializable;
import java.util.*;

/**
 * @author WangLiangChen
 */
public class JsonMap extends Json implements Map<String, Object>, Cloneable, Serializable {
    private final Map<String, Object> map;

    public JsonMap(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        this.map = map;
    }

    public JsonMap() {
        map = new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public JsonMap getJSONMap(String key) {
        Object value = map.get(key);
        if (null == value) {
            return new JsonMap();
        }

        if (value instanceof JsonMap) {
            return (JsonMap) value;
        }

        if (value instanceof Map) {
            return new JsonMap((Map<String, Object>) value);
        }
        throw new RuntimeException("object is not a JSONMap");
    }

    @SuppressWarnings("unchecked")
    public JsonList getJSONList(String key) {
        Object value = map.get(key);
        if (null == value) {
            return new JsonList();
        }
        if (value instanceof JsonList) {
            return (JsonList) value;
        }

        if (value instanceof List) {
            return new JsonList((List<Object>) value);
        }

        throw new RuntimeException("object is not a JSONList");
    }

    public String getString(String key) {
        Object value = get(key);
        if (null == value) {
            return null;
        }
        return String.valueOf(value);
    }

    public Integer getInteger(String key) {
        String value = getString(key);
        if (null == value) {
            return null;
        }
        return Integer.valueOf(value);
    }

    public int getIntValue(String key) {
        Integer value = getInteger(key);
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    public Long getLong(String key) {
        String value = getString(key);
        if (null == value) {
            return null;
        }
        return Long.valueOf(value);
    }

    public long getLongValue(String key) {
        Long value = getLong(key);
        if (value == null) {
            return 0;
        }
        return value.longValue();
    }

    public Float getFloat(String key) {
        String value = getString(key);
        if (null == value) {
            return null;
        }
        return Float.valueOf(value);
    }

    public float getFloatValue(String key) {
        Float value = getFloat(key);
        if (value == null) {
            return 0;
        }
        return value.floatValue();
    }

    public Boolean getBoolean(String key) {
        String value = getString(key);
        if (null == value) {
            return null;
        }
        return Boolean.valueOf(value);
    }

    public boolean getBooleanValue(String key) {
        Boolean value = getBoolean(key);
        if (value == null) {
            return false;
        }
        return value.booleanValue();
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object object = get(key);
        // 使用Json转换
        String mapJson = JsonUtil.INSTANCE.toJsonString(object);
        return JsonUtil.INSTANCE.parseObject(mapJson, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T toJavaObject(Class<T> clazz) {
        if (clazz == Map.class || clazz == JsonMap.class) {
            return (T) this;
        }

        if (clazz == Object.class) {
            return (T) this;
        }
        // 使用Json转换
        String mapJson = JsonUtil.INSTANCE.toJsonString(this.map);
        return JsonUtil.INSTANCE.parseObject(mapJson, clazz);
    }

    public String toJSONString() {
        return JsonUtil.INSTANCE.toJsonString(this.map);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        boolean result = map.containsKey(key);
        if ((!result) && key instanceof Number) {
            result = map.containsKey(key.toString());
        }
        return result;
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        Object val = map.get(key);
        if (val == null && key instanceof Number) {
            val = map.get(key.toString());
        }
        return val;
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Object clone() {
        return new JsonMap(map instanceof LinkedHashMap ? new LinkedHashMap<String, Object>(map) : new HashMap<String, Object>(map));
    }

    @Override
    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}
