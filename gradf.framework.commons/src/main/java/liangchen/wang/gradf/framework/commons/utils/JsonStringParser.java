package liangchen.wang.gradf.framework.commons.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author LiangChen.Wang
 */
public class JsonStringParser {
    private final Gson gson = new GsonBuilder().create();
    private final JsonElement jsonElement;
    private final JsonObject jsonObject;
    private final JsonArray jsonArray;
    private final JsonPrimitive jsonPrimitive;

    public static JsonStringParser newInstance(final String jsonString) {
        return new JsonStringParser(jsonString);
    }

    private JsonStringParser(final String jsonString) {
        this.jsonElement = JsonParser.parseString(jsonString);
        if (this.jsonElement.isJsonObject()) {
            this.jsonObject = jsonElement.getAsJsonObject();
        } else {
            this.jsonObject = null;
        }
        if (this.jsonElement.isJsonArray()) {
            this.jsonArray = this.jsonElement.getAsJsonArray();
        } else {
            this.jsonArray = null;
        }
        if (this.jsonElement.isJsonPrimitive()) {
            this.jsonPrimitive = this.jsonElement.getAsJsonPrimitive();
        } else {
            this.jsonPrimitive = null;
        }
    }

    public <T> T objectByKey(String key, Class<T> clazz) {
        Assert.INSTANCE.notNull(jsonObject, "Json字符串不是JsonObject");
        JsonElement jsonElement = jsonObject.get(key);
        return gson.fromJson(jsonElement, clazz);
    }

    public Object objectByKey(String key, Type rawType, Class clazz) {
        Assert.INSTANCE.notNull(jsonObject, "Json字符串不是JsonObject");
        JsonElement jsonElement = jsonObject.get(key);
        Type type = TypeToken.getParameterized(rawType, clazz).getType();
        return gson.fromJson(jsonElement, type);
    }

    public <T> List<T> collectionByKey(String key, Class<T> clazz) {
        Assert.INSTANCE.notNull(jsonObject, "Json字符串不是JsonObject");
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        JsonElement jsonElement = jsonObject.get(key);
        return gson.fromJson(jsonElement, type);
    }

    public boolean containsKey(String key) {
        return jsonObject.keySet().contains(key);
    }

}
