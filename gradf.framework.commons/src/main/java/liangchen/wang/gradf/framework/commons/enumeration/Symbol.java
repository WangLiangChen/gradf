package liangchen.wang.gradf.framework.commons.enumeration;

/**
 * @author LiangChen.Wang
 */
public enum Symbol {
    /**
     */
    DOT(".", "点号"), POUND("#", "井号"), COMMA(",", "逗号"), COLON(":", "冒号"), SEMICOLON(";", "分号"), HYPHEN("-", "连字号"), UNDERLINE("_", "下划线"), STAR("*", "星号"),
    FILE_SEPARATOR(System.getProperty("file.separator"), "文件分割符"), LINE_SEPARATOR(System.getProperty("line.separator"), "换行符"), PATH_SEPARATOR(System.getProperty("path.separator"), "路径分割符");

    private String symbol;
    private String text;

    Symbol(String symbol, String text) {
        this.symbol = symbol;
        this.text = text;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getText() {
        return text;
    }
}
