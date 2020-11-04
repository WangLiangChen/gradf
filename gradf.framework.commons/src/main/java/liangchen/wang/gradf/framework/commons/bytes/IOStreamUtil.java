package liangchen.wang.gradf.framework.commons.bytes;


import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author LiangChen.Wang 2019/8/18 15:46
 */
public enum IOStreamUtil {
    /**
     * instance
     */
    INSTANCE;
    private final int DEFAULT_BUFFER_SIZE = 8192;
    private final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public <T> void slice(Stream<T> stream, int sliceSize, Consumer<List<T>> consumer) {
        AtomicInteger atomicIndex = new AtomicInteger();
        ArrayList<T>[] container = new ArrayList[1];
        stream.forEach(e -> {
            int index = atomicIndex.getAndIncrement();
            if (0 == index) {
                container[0] = new ArrayList<>();
            }
            container[0].add(e);
            if (index + 2 > sliceSize) {
                atomicIndex.set(0);
                consumer.accept(container[0]);
            }
        });
        //输出剩余数据
        if (container[0].size() < sliceSize) {
            consumer.accept(container[0]);
        }
    }

    public String readString(InputStream inputStream, String encoding, int bufferSize) {
        Assert.INSTANCE.notNull(inputStream, "参数inputStream不能为null");
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }
        if (StringUtil.INSTANCE.isBlank(encoding)) {
            encoding = DEFAULT_CHARSET.name();
        }
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream stream = inputStream) {
            int length;
            while ((length = stream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toString(encoding);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    public String readString(InputStream inputStream, String encoding) {
        return readString(inputStream, encoding, 0);
    }

    public String readString(InputStream inputStream) {
        return readString(inputStream, null, 0);
    }

    public String readString(InputStream inputStream, int bufferSize) {
        return readString(inputStream, null, bufferSize);
    }

    public void io(InputStream inputStream, OutputStream outputStream, int bufferSize) {
        Assert.INSTANCE.notNull(inputStream, "参数inputStream不能为null");
        Assert.INSTANCE.notNull(outputStream, "参数outputStream不能为null");
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }
        byte[] buffer = new byte[bufferSize];
        try (InputStream stream = inputStream) {
            int length;
            while ((length = stream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    public void io(InputStream in, OutputStream out) {
        io(in, out, -1);
    }

    public void io(Reader in, Writer out) {
        io(in, out, -1);
    }

    public void io(Reader reader, Writer writer, int bufferSize) {
        Assert.INSTANCE.notNull(reader, "参数reader不能为null");
        Assert.INSTANCE.notNull(writer, "参数writer不能为null");
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }
        char[] buffer = new char[bufferSize];
        try (Reader r = reader) {
            int length;
            while ((length = r.read(buffer)) >= 0) {
                writer.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    public OutputStream synchronizedOutputStream(OutputStream outputStream) {
        Assert.INSTANCE.notNull(outputStream, "参数outputStream不能为null");
        return new SynchronizedOutputStream(outputStream);
    }

    public OutputStream synchronizedOutputStream(OutputStream outputStream, Object lock) {
        Assert.INSTANCE.notNull(lock, "参数lock不能为null");
        return new SynchronizedOutputStream(outputStream, lock);
    }

    private class SynchronizedOutputStream extends OutputStream {
        private OutputStream out;
        private Object lock;

        SynchronizedOutputStream(OutputStream out) {
            this(out, out);
        }

        SynchronizedOutputStream(OutputStream out, Object lock) {
            this.out = out;
            this.lock = lock;
        }

        @Override
        public void write(int datum) throws IOException {
            synchronized (lock) {
                out.write(datum);
            }
        }

        @Override
        public void write(byte[] data) throws IOException {
            synchronized (lock) {
                out.write(data);
            }
        }

        @Override
        public void write(byte[] data, int offset, int length) throws IOException {
            synchronized (lock) {
                out.write(data, offset, length);
            }
        }

        @Override
        public void flush() throws IOException {
            synchronized (lock) {
                out.flush();
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (lock) {
                out.close();
            }
        }
    }
}
