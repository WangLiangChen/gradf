package liangchen.wang.gradf.framework.commons.exception;

/**
 * @author LiangChen.Wang
 */
public class PromptException extends GradfException {

    public PromptException(String message, Object... args) {
        super(message, args);
    }

    public PromptException(ExceptionData exceptionData, String message, Object... args) {
        super(exceptionData, message, args);
    }

    public PromptException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public PromptException(ExceptionData exceptionData, Throwable cause, String message, Object... args) {
        super(exceptionData, cause, message, args);
    }

    @Override
    public ExceptionMessage getExceptionMessage() {
        ExceptionMessage exceptionMessage = super.getExceptionMessage();
        exceptionMessage.setMessage(this.getMessage());
        exceptionMessage.setDebug(null);
        return exceptionMessage;
    }
}
