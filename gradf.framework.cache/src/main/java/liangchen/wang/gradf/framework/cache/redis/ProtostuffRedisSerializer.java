package liangchen.wang.gradf.framework.cache.redis;

import liangchen.wang.gradf.framework.commons.object.ProtostuffUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class ProtostuffRedisSerializer implements RedisSerializer<Object> {

    @Override
    public byte[] serialize(Object object) throws SerializationException {
        return ProtostuffUtil.INSTANCE.object2Bytes(object);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return ProtostuffUtil.INSTANCE.bytes2Object(bytes);
    }


}
