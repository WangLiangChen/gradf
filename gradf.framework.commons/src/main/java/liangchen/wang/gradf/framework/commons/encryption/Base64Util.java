package liangchen.wang.gradf.framework.commons.encryption;

import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author LiangChen.Wang 2020/9/11
 */
public enum Base64Util {
    /**
     *
     */
    INSTANCE;
    private final Base64.Decoder base64Decoder = Base64.getDecoder();
    private final Base64.Encoder base64Encoder = Base64.getEncoder();

    public byte[] decode(String string) {
        Assert.INSTANCE.notBlank(string, "参数string不能为空");
        return base64Decoder.decode(string);
    }

    public byte[] decode(byte[] bytes) {
        Assert.INSTANCE.notEmpty(bytes, "参数bytes不能为空");
        return base64Decoder.decode(bytes);
    }

    public String encode(byte[] bytes) {
        Assert.INSTANCE.notEmpty(bytes, "参数bytes不能为空");
        return base64Encoder.encodeToString(bytes);
    }

    public String encode(String string, Charset charset) {
        Assert.INSTANCE.notBlank(string, "参数string不能为空");
        if (null == charset) {
            charset = StandardCharsets.UTF_8;
        }
        byte[] bytes = string.getBytes(charset);
        return base64Encoder.encodeToString(bytes);
    }

    public String encode(String string) {
        Assert.INSTANCE.notBlank(string, "参数string不能为空");
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return base64Encoder.encodeToString(bytes);
    }
}
