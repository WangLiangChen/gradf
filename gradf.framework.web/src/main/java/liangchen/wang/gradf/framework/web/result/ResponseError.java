package liangchen.wang.gradf.framework.web.result;


import liangchen.wang.gradf.framework.commons.exception.ExceptionMessage;

/**
 * @author LiangChen.Wang
 */
public class ResponseError extends Response {
    public ResponseError() {
        super(false);
    }

    public ResponseError(ExceptionMessage exceptionMessage) {
        this();
        this.setResult(exceptionMessage);
    }

    public ResponseError(String errorCode, String errorText, Object errorData, String debugText) {
        this();
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setCode(errorCode);
        exceptionMessage.setPayload(errorData);
        exceptionMessage.setMessage(errorText);
        exceptionMessage.setDebug(debugText);
        this.setResult(exceptionMessage);
    }

    public ResponseError(String errorCode, String errorText, String debugText) {
        this(errorCode, errorText, null, debugText);
    }

    public ResponseError(String errorCode, String errorText) {
        this(errorCode, errorText, null);
    }

    public ResponseError(String errorText) {
        this(null, errorText);
    }
}
