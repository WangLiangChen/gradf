package liangchen.wang.gradf.framework.commons.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author WangLiangChen
 */
public enum JSONUtil {
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

    public String toJSONStringWithTransientField(Object object) {
        if (null == object) {
            return null;
        }
        Gson gson = this.gson.newBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();
        return gson.toJson(object);
    }

    public String toJSONString(Object object) {
        if (null == object) {
            return null;
        }
        return gson.toJson(object);
    }

    public <T> T parseObject(String jsonString, Class<T> clazz) {
        return gson.fromJson(jsonString, clazz);
    }

    public Object parseParameterizedObject(String jsonString, Type rawType, Type... types) {
        Type type = TypeToken.getParameterized(rawType, types).getType();
        return gson.fromJson(jsonString, type);
    }

    public Map<String, Object> parseMap(String jsonString) {
        if (StringUtil.INSTANCE.isBlank(jsonString)) {
            return Collections.emptyMap();
        }
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        Assert.INSTANCE.isTrue(jsonElement.isJsonObject(), "Json字符串必须为JsonObject");
        return toMap(jsonElement.getAsJsonObject());
    }

    public List<Object> parseList(String jsonString) {
        if (StringUtil.INSTANCE.isBlank(jsonString)) {
            return Collections.emptyList();
        }
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        Assert.INSTANCE.isTrue(jsonElement.isJsonArray(), "Json字符串必须为JsonArray");
        return toList(jsonElement.getAsJsonArray());
    }

    public JSONMap parseJSONMap(String jsonString) {
        if (StringUtil.INSTANCE.isBlank(jsonString)) {
            return new JSONMap();
        }
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        Assert.INSTANCE.isTrue(jsonElement.isJsonObject(), "Json字符串必须为JsonObject");
        return toJSONMap(jsonElement.getAsJsonObject());
    }

    public JSONList parseJSONList(String jsonString) {
        if (StringUtil.INSTANCE.isBlank(jsonString)) {
            return new JSONList();
        }
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        Assert.INSTANCE.isTrue(jsonElement.isJsonArray(), "Json字符串必须为JsonArray");
        return toJSONList(jsonElement.getAsJsonArray());
    }


    private List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<>();
        json.forEach(e -> {
            if (e.isJsonArray()) {
                list.add(toList((JsonArray) e));
            } else if (e.isJsonObject()) {
                list.add(toMap((JsonObject) e));
            } else if (e.isJsonPrimitive()) {
                list.add(e.getAsString());
            }
        });
        return list;
    }

    private Map<String, Object> toMap(JsonObject json) {
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        entrySet.stream().forEach(e -> {
            String key = e.getKey();
            JsonElement value = e.getValue();
            if (value.isJsonObject()) {
                map.put(key, toMap((JsonObject) value));
            } else if (value.isJsonArray()) {
                map.put(key, toList((JsonArray) value));
            } else if (value.isJsonPrimitive()) {
                map.put(key, value.getAsString());
            } else {
                map.put(key, null);
            }
        });
        return map;
    }

    private JSONList toJSONList(JsonArray json) {
        List<Object> list = new ArrayList<>();
        json.forEach(e -> {
            if (e.isJsonArray()) {
                list.add(toJSONList((JsonArray) e));
            } else if (e.isJsonObject()) {
                list.add(toJSONMap((JsonObject) e));
            } else if (e.isJsonPrimitive()) {
                list.add(e.getAsString());
            }
        });
        return new JSONList(list);
    }

    private JSONMap toJSONMap(JsonObject json) {
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        entrySet.stream().forEach(e -> {
            String key = e.getKey();
            JsonElement value = e.getValue();
            if (value.isJsonObject()) {
                map.put(key, toJSONMap((JsonObject) value));
            } else if (value.isJsonArray()) {
                map.put(key, toJSONList((JsonArray) value));
            } else if (value.isJsonPrimitive()) {
                map.put(key, value.getAsString());
            } else {
                map.put(key, null);
            }
        });
        return new JSONMap(map);
    }
}
