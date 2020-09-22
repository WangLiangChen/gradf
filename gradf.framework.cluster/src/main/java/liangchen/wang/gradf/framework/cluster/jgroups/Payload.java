package liangchen.wang.gradf.framework.cluster.jgroups;

import java.io.Serializable;
import java.util.Objects;

public class Payload implements Serializable {
    private String key;
    private String json;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        }
        if (this == otherObject) {
            return true;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        Payload other = (Payload) otherObject;
        return this.key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.key);
    }
}
