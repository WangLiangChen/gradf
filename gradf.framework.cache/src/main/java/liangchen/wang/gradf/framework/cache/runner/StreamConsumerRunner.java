package liangchen.wang.gradf.framework.cache.runner;

import liangchen.wang.gradf.framework.cache.redis.CacheMessage;
import liangchen.wang.gradf.framework.cache.redis.StringRedisTemplate;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;

/**
 * @author LiangChen.Wang at 2021/3/28 17:28
 */
public class StreamConsumerRunner implements ApplicationRunner, DisposableBean {

    public static final String EXPIRE_CHANNEL = "channel:stream:expire";
    public static final String EXPIRE_GROUP = "group:expire";

    private final ThreadPoolTaskExecutor taskExecutor;
    private final RedisTemplate<String, ObjectRecord<String, CacheMessage>> redisTemplate;
    private final StreamMessageListenerContainer<String, ObjectRecord<String, CacheMessage>> container;

    public StreamConsumerRunner(ThreadPoolTaskExecutor taskExecutor, RedisTemplate<String, ObjectRecord<String, CacheMessage>> redisTemplate) {
        this.taskExecutor = taskExecutor;
        this.redisTemplate = redisTemplate;
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, CacheMessage>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder().targetType(CacheMessage.class)
                        .batchSize(10)
                        .executor(taskExecutor)
                        .pollTimeout(Duration.ZERO)
                        .build();
        StreamMessageListenerContainer<String, ObjectRecord<String, CacheMessage>> container = StreamMessageListenerContainer.create(redisTemplate.getConnectionFactory(), options);
        prepareChannelAndGroup(redisTemplate.opsForStream(), EXPIRE_CHANNEL, EXPIRE_GROUP);
        container.receive(Consumer.from(EXPIRE_GROUP, "consumer-1"), StreamOffset.create(EXPIRE_CHANNEL, ReadOffset.lastConsumed()), new StreamMessageListener(redisTemplate));
        this.container = container;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.container.start();
    }

    @Override
    public void destroy() {
        this.container.stop();
    }

    public static class StreamMessageListener implements StreamListener<String, ObjectRecord<String, CacheMessage>> {
        private final RedisTemplate<String, ObjectRecord<String, CacheMessage>> redisTemplate;

        public StreamMessageListener(RedisTemplate<String, ObjectRecord<String, CacheMessage>> redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        @Override
        public void onMessage(ObjectRecord<String, CacheMessage> message) {
            CacheMessage cacheMessage = message.getValue();
            redisTemplate.opsForStream().acknowledge(EXPIRE_GROUP, message);
        }
    }

    /**
     * 在初始化容器时，如果key对应的stream或者group不存在时会抛出异常，需要提前检查并且初始化。
     *
     * @param ops
     * @param channel
     * @param group
     */
    private void prepareChannelAndGroup(StreamOperations<String, ?, ?> ops, String channel, String group) {
        String status = "OK";
        try {
            StreamInfo.XInfoGroups groups = ops.groups(channel);
            if (groups.stream().noneMatch(xInfoGroup -> group.equals(xInfoGroup.groupName()))) {
                status = ops.createGroup(channel, group);
            }
        } catch (Exception exception) {
            RecordId initialRecord = ops.add(ObjectRecord.create(channel, "Initial Record"));
            Assert.INSTANCE.notNull(initialRecord, "Cannot initialize stream with key '" + channel + "'");
            status = ops.createGroup(channel, ReadOffset.from(initialRecord), group);
        } finally {
            Assert.INSTANCE.isTrue("OK".equals(status), "Cannot create group with name '" + group + "'");
        }
    }
}
