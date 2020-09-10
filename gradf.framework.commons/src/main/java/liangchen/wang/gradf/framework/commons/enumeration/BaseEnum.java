package liangchen.wang.gradf.framework.commons.enumeration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseEnum {
    private final static Map<String,String> map = new ConcurrentHashMap<>();
    private final String name;
    private final String text;

    public BaseEnum(String name, String text) {
        this.name = name;
        this.text = text;
        map.put(this.name,this.text);
    }

    public String name() {
        return name;
    }

    public String text() {
        return text;
    }
    public static String textByName(String name){
        return map.get(name);
    }
}
