package liangchen.wang.gradf.framework.commons.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author WangLiangChen
 */
public enum JsonUtil {
    /**
     *
     */
    INSTANCE;
    /**
     * 注册TypeAdapter 防止数字转化为浮点型
     */
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, (JsonDeserializer<Map>) (json, typeOfT, context) -> {
        Map<String, Object> resultMap = new HashMap<>();
        JsonObject jsonObject = json.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }).create();

    public String toJsonStringWithTransientField(Object object) {
        if (null == object) {
            return null;
        }
        Gson gson = this.gson.newBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();
        return gson.toJson(object);
    }

    public String toJsonString(Object object) {
        if (null == object) {
            return null;
        }
        return gson.toJson(object);
    }

    public <T> T parseObject(String jsonString, Class<T> clazz) {
        return gson.fromJson(jsonString, clazz);
    }

    public <T> T parseObject(JsonMap jsonMap, Class<T> clazz) {
        return parseObject(gson.toJson(jsonMap), clazz);
    }

    public <T> List<T> parseList(String jsonString, Class<T> clazz) {
        return gson.fromJson(jsonString, TypeToken.getParameterized(List.class, clazz).getType());
    }

    public <T> List<T> parseList(JsonList jsonList, Class<T> clazz) {
        return parseList(gson.toJson(jsonList), clazz);
    }

    public <V> Map<String, V> parseMap(String jsonString, Class<V> valueClass) {
        Map<?, ?> map = gson.fromJson(jsonString, Map.class);
        Map<String, V> beanMap = new HashMap<>(map.size());
        map.forEach((k, v) -> {
            String key = String.valueOf(k);
            V value = gson.fromJson((JsonElement) v, valueClass);
            beanMap.put(key, value);
        });
        return beanMap;
    }

    public Object parseParameterizedObject(String jsonString, Type rawType, Type... types) {
        Type type = TypeToken.getParameterized(rawType, types).getType();
        return gson.fromJson(jsonString, type);
    }

    public Object parseParameterizedObject(JsonMap jsonMap, Type rawType, Type... types) {
        return parseParameterizedObject(gson.toJson(jsonMap), rawType, types);
    }


    public JsonMap parseJsonMap(String jsonString) {
        if (StringUtil.INSTANCE.isBlank(jsonString)) {
            return new JsonMap();
        }
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        return toJsonMap(jsonObject);
    }

    public JsonList parseJsonList(String jsonString) {
        if (StringUtil.INSTANCE.isBlank(jsonString)) {
            return new JsonList();
        }
        JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);
        return toJsonList(jsonArray);
    }

    private JsonList toJsonList(JsonArray jsonArray) {
        List<Object> list = new ArrayList<>();
        jsonArray.forEach(e -> {
            if (e.isJsonArray()) {
                list.add(toJsonList((JsonArray) e));
            } else if (e.isJsonObject()) {
                list.add(toJsonMap((JsonObject) e));
            } else if (e.isJsonPrimitive()) {
                list.add(e.getAsString());
            }
        });
        return new JsonList(list);
    }

    private JsonMap toJsonMap(JsonObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        entrySet.stream().forEach(e -> {
            String key = e.getKey();
            JsonElement value = e.getValue();
            if (value.isJsonObject()) {
                map.put(key, toJsonMap((JsonObject) value));
            } else if (value.isJsonArray()) {
                map.put(key, toJsonList((JsonArray) value));
            } else if (value.isJsonPrimitive()) {
                map.put(key, value.getAsString());
            } else {
                map.put(key, null);
            }
        });
        return new JsonMap(map);
    }
}
