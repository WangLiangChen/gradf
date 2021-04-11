package liangchen.wang.gradf.framework.cache.redis;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class ProtostuffRedisSerializer implements RedisSerializer<Object> {
    final Schema<ValueWrapper> schema = RuntimeSchema.getSchema(ValueWrapper.class);

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return null;
    }

    public byte[] encode(Object value) {
        final LinkedBuffer buffer = LinkedBuffer.allocate();
        return ProtostuffIOUtil.toByteArray(new ValueWrapper(value), schema, buffer);
    }

    public Object decode(byte[] bytes) {
        ValueWrapper valueWrapper = new ValueWrapper();
        ProtostuffIOUtil.mergeFrom(bytes, valueWrapper, schema);
        return valueWrapper.getData();
    }

    public static class ValueWrapper {
        private Object data;

        public ValueWrapper() {

        }

        public ValueWrapper(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
