package liangchen.wang.gradf.framework.commons.exception;

/**
 * @author liangchen.wang 2020/09/10
 */
public class ExceptionMessage extends ExceptionData {
    /**
     * 调试信息
     */
    private String debug;
    /**
     * 错误提示信息
     */
    private String message;

    public ExceptionMessage(ExceptionData exceptionData) {
        if (null != exceptionData) {
            setCode(exceptionData.getCode());
            setPayload(exceptionData.getPayload());
        }
    }

    public ExceptionMessage() {
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
