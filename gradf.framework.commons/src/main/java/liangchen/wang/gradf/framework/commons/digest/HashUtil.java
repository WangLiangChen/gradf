package liangchen.wang.gradf.framework.commons.digest;


import liangchen.wang.gradf.framework.commons.bytes.ByteUtil;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author LiangChen.Wang
 */
public enum HashUtil {
    /**
     * INSTANCE
     */
    INSTANCE;

    public int hashCode(Object... objects) {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(objects);
        return hashCodeBuilder.hashCode();
    }

    public int hash(Object object) {
        Assert.INSTANCE.notNull(object, "参数object不能为null");
        int h = object.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    public int hashIndex(final Object object, int indexScope) {
        Assert.INSTANCE.notNull(object, "参数object不能为null");
        int number = indexScope & (indexScope - 1);
        Assert.INSTANCE.isTrue(number == 0, "indexScope must be a power of 2");
        return hash(object) & (indexScope - 1);
    }

    public String digest(String content, String algorithm) {
        Assert.INSTANCE.notBlank(content, "参数content不能为空");
        Assert.INSTANCE.notBlank(algorithm, "参数algorithm不能为空");
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new ErrorException(e);
        }
        messageDigest.update(content.getBytes(StandardCharsets.UTF_8));
        return ByteUtil.INSTANCE.bytes2HexString(messageDigest.digest());
    }


    public String md5Digest(String string) {
        Assert.INSTANCE.notNull(string, "参数string不能为null");
        return digest(string, "MD5");
    }

    public String md5Digest16(String string) {
        return md5Digest(string).substring(8, 24);
    }

    public String sha256Digest(String key, String content) {
        Assert.INSTANCE.notBlank(key, "参数content不能为空");
        Assert.INSTANCE.notBlank(content, "参数content不能为空");
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = key.getBytes("utf-8");
            byte[] dataBytes = content.getBytes("utf-8");

            SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");
            mac.init(secret);

            byte[] doFinal = mac.doFinal(dataBytes);
            return ByteUtil.INSTANCE.bytes2HexString(doFinal);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }
}
