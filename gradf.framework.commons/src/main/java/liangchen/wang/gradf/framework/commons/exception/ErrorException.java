package liangchen.wang.gradf.framework.commons.exception;

/**
 * @author LiangChen.Wang
 */
public class ErrorException extends GradfException {

    public ErrorException(String message, Object... args) {
        super(message, args);
    }

    public ErrorException(ExceptionData exceptionData, String message, Object... args) {
        super(exceptionData, message, args);
    }

    public ErrorException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public ErrorException(ExceptionData exceptionData, Throwable cause, String message, Object... args) {
        super(exceptionData, cause, message, args);
    }

    public ErrorException(Throwable cause) {
        super(cause);
    }
}
