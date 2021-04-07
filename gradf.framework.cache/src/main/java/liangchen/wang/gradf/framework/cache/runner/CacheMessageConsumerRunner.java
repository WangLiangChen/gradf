package liangchen.wang.gradf.framework.cache.runner;

import liangchen.wang.gradf.framework.cache.redis.CacheMessage;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.Duration;
import java.util.concurrent.Executor;

/**
 * @author LiangChen.Wang at 2021/3/28 17:28
 */
@Component
@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
public class CacheMessageConsumerRunner implements ApplicationRunner, DisposableBean {

    public static final String EXPIRE_CHANNEL = "channel:stream:expire";
    public static final String EXPIRE_GROUP = "group:expire";
    private final StreamMessageListenerContainer<String, ObjectRecord<String, CacheMessage>> container;

    @Inject
    public CacheMessageConsumerRunner(Executor taskExecutor, StringRedisTemplate stringRedisTemplate) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, CacheMessage>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                        .targetType(CacheMessage.class)
                        .batchSize(10)
                        .executor(taskExecutor)
                        .pollTimeout(Duration.ZERO)
                        .build();
        StreamMessageListenerContainer<String, ObjectRecord<String, CacheMessage>> container = StreamMessageListenerContainer.create(stringRedisTemplate.getConnectionFactory(), options);
        prepareChannelAndGroup(stringRedisTemplate);
        container.receiveAutoAck(Consumer.from(EXPIRE_GROUP, "CacheMessageConsumer"), StreamOffset.create(EXPIRE_CHANNEL, ReadOffset.lastConsumed()), new StreamMessageListener(stringRedisTemplate));
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
        private final StringRedisTemplate stringRedisTemplate;

        public StreamMessageListener(StringRedisTemplate stringRedisTemplate) {
            this.stringRedisTemplate = stringRedisTemplate;
        }

        @Override
        public void onMessage(ObjectRecord<String, CacheMessage> message) {
            CacheMessage cacheMessage = message.getValue();
            System.out.println("----------------------------cacheMessage:" + JsonUtil.INSTANCE.toJsonString(cacheMessage));
            // redisTemplate.opsForStream().acknowledge(EXPIRE_GROUP, message);
        }
    }

    /**
     * 在初始化容器时，如果key对应的stream或者group不存在时会抛出异常，需要提前检查并且初始化
     */
    private void prepareChannelAndGroup(StringRedisTemplate stringRedisTemplate) {
        String status = "OK";
        StreamOperations<String, Object, Object> streamOperations = stringRedisTemplate.opsForStream();
        try {
            StreamInfo.XInfoGroups groups = streamOperations.groups(EXPIRE_CHANNEL);
            if (groups.stream().noneMatch(group -> EXPIRE_GROUP.equals(group.groupName()))) {
                status = streamOperations.createGroup(EXPIRE_CHANNEL, EXPIRE_GROUP);
            }
        } catch (Exception exception) {
            ObjectRecord<String, CacheMessage> record = StreamRecords.newRecord().ofObject(CacheMessage.newInstance("name", CacheMessage.CacheAction.none)).withStreamKey(EXPIRE_CHANNEL);
            RecordId initialRecord = streamOperations.add(record);
            Assert.INSTANCE.notNull(initialRecord, "Cannot initialize stream with key '" + EXPIRE_CHANNEL + "'");
            status = streamOperations.createGroup(EXPIRE_CHANNEL, ReadOffset.from(initialRecord), EXPIRE_GROUP);
        } finally {
            Assert.INSTANCE.isTrue("OK".equals(status), "Cannot create group with name '" + EXPIRE_GROUP + "'");
        }
    }
}
