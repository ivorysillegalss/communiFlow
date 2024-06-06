package org.chenzc.communi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

/**
 * 消息队列工具类
 *
 * @author chenz
 * @date 2024/06/06
 */
@Slf4j
public class MessageQueueUtils {
    private MessageQueueUtils() {
    }

    /**
     * 获取一个kafka消费者作为Flink的源的初始化配置
     *
     * @param topicName
     * @param groupId
     * @return
     */
    public static KafkaSource<String> getKafkaConsumer(String topicName, String groupId, String broker) {
        return KafkaSource.<String>builder()
                .setBootstrapServers(broker)
                .setTopics(topicName)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
    }
}
