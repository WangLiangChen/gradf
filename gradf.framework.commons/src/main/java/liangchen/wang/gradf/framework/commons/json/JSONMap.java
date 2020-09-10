package liangchen.wang.gradf.framework.commons.json;

import java.io.Serializable;
import java.util.*;

public class JSONMap extends JSON implements Map<String, Object>, Cloneable, Serializable {
    private final Map<String, Object> map;

    public JSONMap(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        this.map = map;
    }

    public JSONMap() {
        map = new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public JSONMap getJSONMap(String key) {
        Object value = map.get(key);
        if (null == value) {
            return new JSONMap();
        }

        if (value instanceof JSONMap) {
            return (JSONMap) value;
        }

        if (value instanceof Map) {
            return new JSONMap((Map<String, Object>) value);
        }
        throw new RuntimeException("object is not a JSONMap");
    }

    @SuppressWarnings("unchecked")
    public JSONList getJSONList(String key) {
        Object value = map.get(key);
        if (null == value) {
            return new JSONList();
        }
        if (value instanceof JSONList) {
            return (JSONList) value;
        }

        if (value instanceof List) {
            return new JSONList((List<Object>) value);
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

    @SuppressWarnings("unchecked")
    public <T> T toJavaObject(Class<T> clazz) {
        if (clazz == Map.class || clazz == JSONMap.class) {
            return (T) this;
        }

        if (clazz == Object.class) {
            return (T) this;
        }
        // 使用Json转换
        String mapJson = JSONUtil.INSTANCE.toJSONString(this.map);
        return JSONUtil.INSTANCE.parseObject(mapJson, clazz);
    }

    public String toJSONString() {
        return JSONUtil.INSTANCE.toJSONString(this.map);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        boolean result = map.containsKey(key);
        if ((!result) && key instanceof Number) {
            result = map.containsKey(key.toString());
        }
        return result;
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Object get(Object key) {
        Object val = map.get(key);
        if (val == null && key instanceof Number) {
            val = map.get(key.toString());
        }
        return val;
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        map.putAll(m);
    }

    public void clear() {
        map.clear();
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Collection<Object> values() {
        return map.values();
    }

    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Object clone() {
        return new JSONMap(map instanceof LinkedHashMap ? new LinkedHashMap<String, Object>(map) : new HashMap<String, Object>(map));
    }

    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    public int hashCode() {
        return this.map.hashCode();
    }

}
