package liangchen.wang.gradf.framework.commons.enumeration;

/**
 * @author wangliangchen
 * 参见 java.lang.reflect.Modifier
 */
public enum ModifierEnum {
    /**
     *
     */
    PUBLIC(0x00000001),
    PRIVATE(0x00000002),
    PROTECTED(0x00000004),
    STATIC(0x00000008),

    FINAL(0x00000010),
    SYNCHRONIZED(0x00000020),
    VOLATILE(0x00000040),
    TRANSIENT(0x00000080),

    NATIVE(0x00000100),
    INTERFACE(0x00000200),
    ABSTRACT(0x00000400),
    STRICT(0x00000800);

    // 通过移位运算的值
    private final int shiftValue;
    // 直接设置的16进制值
    private final int finalValue;

    ModifierEnum(int finalValue) {
        this.finalValue = finalValue;
        this.shiftValue = 1 << this.ordinal();
    }

    public int getShiftValue() {
        return shiftValue;
    }

    public int getFinalValue() {
        return finalValue;
    }
}
