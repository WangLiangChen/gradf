package liangchen.wang.gradf.framework.commons.utils;


import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author WangLiangChen
 */
public enum BigDecimalUtil {
    /**
     * instance
     */
    INSTANCE;

    public BigDecimal add(String v1, String v2, int bit) {
        Assert.INSTANCE.notBlank(v1, "参数v1不能为空");
        Assert.INSTANCE.notBlank(v2, "参数v2不能为空");
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return add(b1, b2, bit);
    }

    public BigDecimal add(BigDecimal v1, BigDecimal v2, int scale) {
        return calculate(v1, v2, scale, Operator.ADD);
    }

    public BigDecimal subtract(String v1, String v2, int bit) {
        Assert.INSTANCE.notBlank(v1, "参数v1不能为空");
        Assert.INSTANCE.notBlank(v2, "参数v2不能为空");
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return subtract(b1, b2, bit);
    }

    public BigDecimal subtract(BigDecimal v1, BigDecimal v2, int scale) {
        return calculate(v1, v2, scale, Operator.SUBTRACT);
    }

    public BigDecimal multiply(String v1, String v2, int bit) {
        Assert.INSTANCE.notBlank(v1, "参数v1不能为空");
        Assert.INSTANCE.notBlank(v2, "参数v2不能为空");
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return multiply(b1, b2, bit);
    }

    public BigDecimal multiply(BigDecimal v1, BigDecimal v2, int scale) {
        return calculate(v1, v2, scale, Operator.MULTIPLY);
    }

    public BigDecimal divide(String v1, String v2, int bit) {
        Assert.INSTANCE.notBlank(v1, "参数v1不能为空");
        Assert.INSTANCE.notBlank(v2, "参数v2不能为空");
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return divide(b1, b2, bit);
    }

    public BigDecimal divide(BigDecimal v1, BigDecimal v2, int scale) {
        return calculate(v1, v2, scale, Operator.DIVIDE);
    }

    private BigDecimal calculate(BigDecimal v1, BigDecimal v2, int scale, Operator operator) {
        Assert.INSTANCE.notNull(v1, "参数v1不能为null");
        Assert.INSTANCE.notNull(v2, "参数v2不能为null");
        Assert.INSTANCE.isTrue(scale > -1, "保留位数必须大于等于0");
        switch (operator) {
            case ADD:
                return v1.add(v2).setScale(scale, RoundingMode.HALF_UP);
            case SUBTRACT:
                return v1.subtract(v2).setScale(scale, RoundingMode.HALF_UP);
            case MULTIPLY:
                return v1.multiply(v2).setScale(scale, RoundingMode.HALF_UP);
            case DIVIDE:
                return v1.divide(v2).setScale(scale, RoundingMode.HALF_UP);
            default:
                return new BigDecimal(0);
        }
    }

    public BigDecimal round(BigDecimal v) {
        Assert.INSTANCE.notNull(v, "参数v不能为null");
        return v.divide(new BigDecimal(1)).setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal calculate(BigDecimal v, int scale, Operator operator) {
        Assert.INSTANCE.notNull(v, "参数v不能为null");
        Assert.INSTANCE.isTrue(scale > -1, "保留位数必须大于等于0");
        switch (operator) {
            case ABS:
                return v.setScale(scale, RoundingMode.HALF_UP).abs();
            default:
                return new BigDecimal(0);
        }
    }

    private enum Operator {
        /**
         * 运算符
         */
        ADD, SUBTRACT, MULTIPLY, DIVIDE, ABS, ROUND;
    }

}
