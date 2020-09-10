package liangchen.wang.gradf.framework.commons.exception;

import liangchen.wang.gradf.framework.commons.utils.StringUtil;

/**
 * @author LiangChen.Wang
 */
public abstract class GradfException extends RuntimeException {
    private ExceptionData exceptionData;

    public GradfException(String message, Object... args) {
        super(StringUtil.INSTANCE.format(message, args));
    }

    public GradfException(ExceptionData exceptionData, String message, Object... args) {
        super(StringUtil.INSTANCE.format(message, args));
        this.exceptionData = exceptionData;
    }

    public GradfException(Throwable cause, String message, Object... args) {
        super(StringUtil.INSTANCE.format(message, args), cause);
    }

    public GradfException(ExceptionData exceptionData, Throwable cause, String message, Object... args) {
        super(StringUtil.INSTANCE.format(message, args), cause);
        this.exceptionData = exceptionData;
    }

    public GradfException(Throwable cause) {
        super(cause);
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        StackTraceElement[] stackTrace = super.getStackTrace();
        int length = stackTrace.length;
        length = length > 10 ? 10 : length;
        StackTraceElement[] newArray = new StackTraceElement[length];
        for (int i = 0; i < length; i++) {
            newArray[i] = stackTrace[i];
        }
        return newArray;
    }

    public ExceptionMessage getExceptionMessage() {
        ExceptionMessage exceptionMessage = new ExceptionMessage(exceptionData);
        final String errorText = "系统错误，请稍后重试或联系管理员";
        exceptionMessage.setMessage(errorText);
        exceptionMessage.setDebug(super.getMessage());
        return exceptionMessage;
    }
}
