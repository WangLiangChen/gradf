package liangchen.wang.gradf.framework.commons.object;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author LiangChen.Wang 2021/4/12
 */
public enum ProtostuffUtil {
    //
    INSTANCE;
    private final Schema<ValueWrapper> schema;

    ProtostuffUtil() {
        schema = RuntimeSchema.getSchema(ValueWrapper.class);
    }

    public byte[] object2Bytes(Object value) {
        final LinkedBuffer buffer = LinkedBuffer.allocate();
        return ProtostuffIOUtil.toByteArray(new ValueWrapper(value), schema, buffer);
    }

    public Object bytes2Object(byte[] bytes) {
        ValueWrapper valueWrapper = new ValueWrapper();
        ProtostuffIOUtil.mergeFrom(bytes, valueWrapper, schema);
        return valueWrapper.getData();
    }

    public static class ValueWrapper<T> {
        private T data;

        public ValueWrapper() {

        }

        public ValueWrapper(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
