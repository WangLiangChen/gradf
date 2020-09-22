package liangchen.wang.gradf.framework.commons.utils;

/**
 * @author liangchen.wang 2020/9/10
 */
public enum StringUtil {
    /**
     *
     */
    INSTANCE;
    private final String FORMAT_REGEX = "\\{(.*?)\\}";
    private final String FORMAT_REPLACEMENT = "\\%s";

    public boolean isNull(String string) {
        return null == string;
    }

    public boolean isNotNull(String string) {
        return null != string;
    }

    public boolean isBlank(String string) {
        if (null == string) {
            return true;
        }
        return string.trim().length() == 0;
    }

    public boolean isNotBlank(String string) {
        if (null == string) {
            return false;
        }
        return string.trim().length() > 0;
    }

    /**
     * 按顺序替换{}或者{.*}
     *
     * @param format
     * @param args
     * @return 替换后的字符串
     */
    public String format(String format, Object... args) {
        if (isBlank(format)) {
            return format;
        }
        format = format.replaceAll(FORMAT_REGEX, FORMAT_REPLACEMENT);
        return String.format(format, args);
    }

    public String firstLetterLowerCase(String string) {
        char[] chars = string.toCharArray();
        char first = chars[0];
        if (first >= 'A' && first <= 'Z') {
            first = (char) (first + 32);
        }
        chars[0] = first;
        return new String(chars);
    }

    public String firstLetterUpperCase(String string) {
        char[] chars = string.toCharArray();
        char first = chars[0];
        if (first >= 'a' && first <= 'z') {
            first = (char) (first - 32);
        }
        chars[0] = first;
        return new String(chars);
    }

    public String firstLetterConvertCase(String string) {
        char[] chars = string.toCharArray();
        char first = chars[0];
        if (first >= 'a' && first <= 'z') {
            first = (char) (first - 32);
        } else if (first >= 'A' && first <= 'Z') {
            first = (char) (first + 32);
        }
        chars[0] = first;
        return new String(chars);
    }

    public String clearBlank(String source) {
        if (null == source) {
            return null;
        }
        return source.replaceAll("\\s", "");
    }
}
