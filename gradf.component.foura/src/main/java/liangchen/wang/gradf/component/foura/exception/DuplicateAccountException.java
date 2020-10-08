package liangchen.wang.gradf.component.foura.exception;


import liangchen.wang.gradf.framework.commons.exception.ExceptionData;
import liangchen.wang.gradf.framework.commons.exception.PromptException;

/**
 * @author WangLiangChen
 */
public class DuplicateAccountException extends PromptException {
    public DuplicateAccountException(String message, Object... args) {
        super(message, args);
    }

    public DuplicateAccountException(ExceptionData exceptionData, String message, Object... args) {
        super(exceptionData, message, args);
    }

    public DuplicateAccountException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public DuplicateAccountException(ExceptionData exceptionData, Throwable cause, String message, Object... args) {
        super(exceptionData, cause, message, args);
    }
}
