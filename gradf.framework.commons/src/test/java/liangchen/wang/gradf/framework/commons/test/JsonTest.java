package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.json.JsonList;
import liangchen.wang.gradf.framework.commons.json.JsonMap;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @author liangchen.wang 2020/9/10
 */
public class JsonTest {

    @Test
    public void testList() {
        String jsonString = "[{\"a\":\"a\",\"b\":1},{\"a\":\"aa\",\"b\":2}]";
        List<JsonBean> jsonBeans = JsonUtil.INSTANCE.parseList(jsonString, JsonBean.class);
        System.out.println(jsonBeans);
    }

    @Test
    public void testMap() {
        String jsonString = "{\"key1\":{\"a\":\"a\",\"b\":1},\"key2\":{\"a\":\"aa\",\"b\":2}}";
        Map<String, JsonBean> jsonBeanMap = JsonUtil.INSTANCE.parseMap(jsonString, JsonBean.class);
        System.out.println(jsonBeanMap);
    }

    @Test
    public void testJsonList() {
        String jsonString = "[{\"a\":\"a\",\"b\":1},{\"a\":\"aa\",\"b\":2}]";
        JsonList jsonList = JsonUtil.INSTANCE.parseJsonList(jsonString);
        System.out.println(jsonList);
    }

    @Test
    public void testJsonMap() {
        String jsonString = "{\"key1\":{\"a\":\"a\",\"b\":1},\"key2\":{\"a\":\"aa\",\"b\":2}}";
        JsonMap jsonMap = JsonUtil.INSTANCE.parseJsonMap(jsonString);
        System.out.println(jsonMap);
    }

}
