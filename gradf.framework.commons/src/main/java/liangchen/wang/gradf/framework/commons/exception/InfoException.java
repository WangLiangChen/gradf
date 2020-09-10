package liangchen.wang.gradf.framework.commons.exception;

/**
 * @author LiangChen.Wang
 */
public class InfoException extends GradfException {

    public InfoException(String message, Object... args) {
        super(message, args);
    }

    public InfoException(ExceptionData exceptionData, String message, Object... args) {
        super(exceptionData, message, args);
    }

    public InfoException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public InfoException(ExceptionData exceptionData, Throwable cause, String message, Object... args) {
        super(exceptionData, cause, message, args);
    }
}
