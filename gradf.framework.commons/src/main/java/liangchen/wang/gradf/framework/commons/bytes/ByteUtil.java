package liangchen.wang.gradf.framework.commons.bytes;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author LiangChen.Wang
 */
public enum ByteUtil {
    /**
     *
     */
    INSTANCE;
    private final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private final Kryo kryo = new Kryo();

    public byte[] toBytes(Object object) {
        Assert.INSTANCE.notNull(object, "参数object不能为空");
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Output output = new Output(outputStream);
            kryo.writeObject(output, object);
            byte[] bytes = output.toBytes();
            output.flush();
            return bytes;
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    public byte[] toBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public byte[] toBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public byte[] toBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public byte[] toBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public byte[] toBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return toBytes(intBits);
    }

    public byte[] toBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return toBytes(intBits);
    }

    public byte[] toBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    public byte[] toBytes(String data) {
        if (StringUtil.INSTANCE.isBlank(data)) {
            return new byte[0];
        }
        return data.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] toBytes(String data, Charset charset) {
        if (StringUtil.INSTANCE.isBlank(data)) {
            return new byte[0];
        }
        Assert.INSTANCE.notNull(charset, "参数charset不能为空");
        return data.getBytes(charset);
    }

    public short toShort(byte[] bytes) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public char toChar(byte[] bytes) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public int toInt(byte[] bytes) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public long toLong(byte[] bytes) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16)) | (0xff000000L & ((long) bytes[3] << 24))
                | (0xff00000000L & ((long) bytes[4] << 32)) | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48)) | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public float toFloat(byte[] bytes) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        return Float.intBitsToFloat(toInt(bytes));
    }

    public double toDouble(byte[] bytes) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        long l = toLong(bytes);
        return Double.longBitsToDouble(l);
    }

    public String toString(byte[] bytes) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public String toString(byte[] bytes, Charset charset) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        Assert.INSTANCE.notNull(charset, "参数charset不能为null");
        return new String(bytes, charset);
    }

    public <T> T toObject(byte[] bytes, Class<T> clazz) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        Assert.INSTANCE.notNull(clazz, "参数clazz不能为null");
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            Kryo kryo = new Kryo();
            Input input = new Input(inputStream);
            T object = kryo.readObject(input, clazz);
            input.close();
            return object;
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    public String bytes2HexString(byte[] bytes) {
        Assert.INSTANCE.notNull(bytes, "参数byte不能为null");
        StringBuilder resultSb = new StringBuilder();
        for (byte b : bytes) {
            resultSb.append(byte2HexString(b));
        }
        return resultSb.toString();
    }

    public String byte2HexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
