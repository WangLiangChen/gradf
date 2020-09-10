package liangchen.wang.gradf.framework.commons.validator;

import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author .LiangChen.Wang
 */
public enum Assert {
    INSTANCE;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public void notNull(Object object, Runnable runnable) {
        if (object != null) {
            return;
        }
        assertRunnable(runnable);
    }

    public void notNull(Object object, String message, Object... args) {
        if (object != null) {
            return;
        }
        assertException(message, args);
    }

    public void isNull(Object object, Runnable runnable) {
        if (object == null) {
            return;
        }
        assertRunnable(runnable);
    }

    public void isNull(Object object, String message, Object... args) {
        if (object == null) {
            return;
        }
        assertException(message, args);
    }

    public void notBlank(String string, Runnable runnable) {
        if (StringUtil.INSTANCE.isNotBlank(string)) {
            return;
        }
        assertRunnable(runnable);
    }

    public void notBlank(String string, String message, Object... args) {
        if (StringUtil.INSTANCE.isNotBlank(string)) {
            return;
        }
        assertException(message, args);
    }

    public void isBlank(String string, String message, Object... args) {
        if (StringUtil.INSTANCE.isBlank(string)) {
            return;
        }
        assertException(message, args);
    }

    public <T> void isEquals(T parama, T paramb, String message, Object... args) {
        if (null == parama && null == paramb) {
            return;
        }
        if (null != parama && null != paramb && parama.equals(paramb)) {
            return;
        }
        assertException(message, args);
    }

    public <T> void notEquals(T parama, T paramb, String message, Object... args) {
        if (null == parama && null != paramb) {
            return;
        }
        if (null != parama && null == paramb) {
            return;
        }
        if (null != parama && null != paramb && !parama.equals(paramb)) {
            return;
        }
        assertException(message, args);
    }

    public void isTrue(boolean condition, String message, Object... args) {
        if (condition) {
            return;
        }
        assertException(message, args);
    }

    public void isFalse(boolean condition, String message, Object... args) {
        if (!condition) {
            return;
        }
        assertException(message, args);
    }

    public void matchRegex(String string, String patternStr, String message, Object... args) {
        boolean match = matchRegex(string, patternStr);
        if (match) {
            return;
        }
        assertException(message, args);
    }

    public void notMatchRegex(String string, String patternStr, String message, Object... args) {
        boolean match = matchRegex(string, patternStr);
        if (!match) {
            return;
        }
        assertException(message, args);
    }

    private boolean matchRegex(String string, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public void validate(Object object, Class... groupsClass) {
        if (null == object) {
            return;
        }
        Class<?>[] groups = resolveGroups(groupsClass);
        Set<ConstraintViolation<Object>> violations = validator.validate(object, groups);
        if (CollectionUtil.INSTANCE.isEmpty(violations)) {
            return;
        }
        StringBuilder messages = new StringBuilder();
        violations.forEach(e -> messages.append(e.getMessage()).append(Symbol.SEMICOLON.getSymbol()));
        assertException(messages.toString());
    }

    private static Class<?>[] resolveGroups(Class<?>... groupsClass) {
        Class[] groups;
        if (CollectionUtil.INSTANCE.isEmpty(groupsClass)) {
            groups = new Class[]{Default.class};
        } else {
            groups = new Class[groupsClass.length + 1];
            System.arraycopy(groupsClass, 0, groups, 0, groupsClass.length);
            groups[groups.length - 1] = Default.class;
        }
        return groups;
    }

    private void assertException(String message, Object... args) {
        throw new InfoException(message, args);
    }

    private void assertRunnable(Runnable runnable) {
        if (null == runnable) {
            return;
        }
        runnable.run();
    }
}
