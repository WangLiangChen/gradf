package liangchen.wang.gradf.framework.web.jwts;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiangChen.Wang
 */
public class AccessToken {
    //如果直接继承HasMap,spring会先使用自己的resolver，造成类型匹配错误，所以这里要包装一下
    private Map<String, Object> map = new HashMap<>();

    public static AccessToken newInstance() {
        return new AccessToken();
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
