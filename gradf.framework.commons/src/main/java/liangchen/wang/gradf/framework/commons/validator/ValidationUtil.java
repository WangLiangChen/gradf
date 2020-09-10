package liangchen.wang.gradf.framework.commons.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liangchen.wang 2020/9/10
 */
public enum ValidationUtil {
    INSTANCE;
    private final Pattern loginNamePattern = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z][\\u4e00-\\u9fa5a-zA-Z0-9_-]{4,14}$");
    private final Pattern mobilePattern = Pattern.compile("^1[34578][0-9]\\d{4,8}$");
    private final Pattern identityPattern = Pattern.compile("^\\d{15}$|^\\d{17}[0-9Xx]$");

    public boolean isPassword(String password, AssertLevel assertLevel) {
        Assert.INSTANCE.notBlank(password, "密码不能为空");
        //是否包含数字：包含返回1，不包含返回0
        int existNumber = password.matches(".*\\d+.*") ? 1 : 0;
        //是否包含小写字母：包含返回1，不包含返回0
        int existLower = password.matches(".*[a-z]+.*") ? 1 : 0;
        int existUpper = password.matches(".*[A-Z]+.*") ? 1 : 0;
        //是否包含特殊符号(~!@#$%^&*()_+|<>,.?/:;'[]{}\)：包含返回1，不包含返回0
        int existSymbol = password.matches(".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*") ? 1 : 0;
        int length = password.length();
        boolean match = existNumber + existLower + existUpper + existSymbol >= 3 && length >= 6 && length <= 16;
        return match;
    }

    public boolean isMobile(String mobile, AssertLevel assertLevel) {
        Assert.INSTANCE.notBlank(mobile, "手机号不能为空");
        Matcher matcher = mobilePattern.matcher(mobile);
        boolean matches = matcher.matches();
        return matches;
    }

    public boolean isIdentity(String identity) {
        Assert.INSTANCE.notBlank(identity, "身份证号不能为空");
        Matcher matcher = identityPattern.matcher(identity);
        if (!matcher.matches()) {
            return false;
        }
        char[] id = identity.toCharArray();
        int sum = 0;
        int w[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] ch = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        for (int i = 0; i < id.length - 1; i++) {
            sum += (id[i] - '0') * w[i];
        }
        int c = sum % 11;
        char code = ch[c];
        char last = id[id.length - 1];
        last = last == 'x' ? 'X' : last;
        if (last == code) {
            return true;
        }
        return false;
    }
}
