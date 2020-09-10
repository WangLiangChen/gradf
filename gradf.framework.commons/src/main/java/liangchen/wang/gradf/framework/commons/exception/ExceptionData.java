package liangchen.wang.gradf.framework.commons.exception;

import java.io.Serializable;

/**
 * @author liangchen.wang 2020/09/10
 */
public class ExceptionData implements Serializable {
    /**
     * 错误代码
     */
    private String code;
    /**
     * 错误数据 将会被序列化为JSON
     */
    private Object payload;

    public static ExceptionData newInstance(String code) {
        ExceptionData exceptionData = new ExceptionData();
        exceptionData.code = code;
        return exceptionData;
    }

    public static ExceptionData newInstance(String code, Object payload) {
        ExceptionData exceptionData = new ExceptionData();
        exceptionData.code = code;
        exceptionData.payload = payload;
        return exceptionData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

}
