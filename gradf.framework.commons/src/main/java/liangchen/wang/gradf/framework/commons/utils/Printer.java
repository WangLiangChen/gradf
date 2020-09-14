package liangchen.wang.gradf.framework.commons.utils;

import java.util.HashMap;
import java.util.Map;

public enum Printer {
    /**
     *
     */
    INSTANCE;

    public void prettyPrint(String message, Object... args) {
        prettyPrint(message, false, args);
    }

    public void prettyPrint(String message, boolean isError, Object... args) {
        Map<String, String> stack = stack();
        String text = String.format(" ------ %s, %s - %s#%s:%s", DateTimeUtil.INSTANCE.getHH_MM_SS_SSS(), StringUtil.INSTANCE.format(message, args), stack.get("className"), stack.get("methodName"), stack.get("lineNumber"));
        if (isError) {
            System.err.println(text);
        } else {
            System.out.println(text);
        }
    }

    private Map<String, String> stack() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String className = null;
        String methodName = null;
        int lineNumber = 0;
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            className = stackTraceElement.getClassName();
            if ("java.lang.Thread".equals(className) || Printer.class.getName().equals(className)) {
                continue;
            }
            methodName = stackTraceElement.getMethodName();
            lineNumber = stackTraceElement.getLineNumber();
            break;
        }
        Map<String, String> map = new HashMap<>();
        className = className.substring(className.lastIndexOf(".") + 1);
        map.put("className", className);
        map.put("methodName", methodName);
        map.put("lineNumber", Integer.toString(lineNumber));
        return map;
    }
}
