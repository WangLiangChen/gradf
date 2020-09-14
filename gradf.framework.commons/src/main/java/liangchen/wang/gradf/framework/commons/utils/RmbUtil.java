package liangchen.wang.gradf.framework.commons.utils;

import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.math.BigDecimal;

/**
 * @author LiangChen.Wang 2020/9/14
 */
public enum RmbUtil {
    /**
     * instance
     */
    INSTANCE;

    public String fen2Yuan(Long fen) {
        Assert.INSTANCE.notNull(fen, "参数不能为空");
        if (0 == fen) {
            return "0";
        }
        BigDecimal bigDecimal = BigDecimalUtil.INSTANCE.divide(String.valueOf(fen), "100", 2);
        return bigDecimal.toString();
    }

    public String fen2Yuan(Integer fen) {
        Assert.INSTANCE.notNull(fen, "参数不能为空");
        if (0 == fen) {
            return "0";
        }
        return fen2Yuan(fen.longValue());
    }

    public Long yuan2Fen(String yuan) {
        Assert.INSTANCE.notBlank(yuan, "参数不能为空");
        BigDecimal bigDecimal = BigDecimalUtil.INSTANCE.multiply(yuan, "100", 0);
        return bigDecimal.longValue();
    }
}
