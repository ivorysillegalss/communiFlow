package org.chenzc.communi.receiver.kafka;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.push.PushService;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.utils.GroupIdMappingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * kafka的消费层设计
 *
 * @author chenz
 * @date 2024/05/27
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ConditionalOnProperty(name = "communi.mq.pipeline", havingValue = CommonConstant.KAFKA)
public class KafkaReceiver {

    @Resource
    private PushService pushService;

    /**
     * 消费信息的方法
     *
     * @param consumerRecord
     * @param topicGroupId
     */
    @KafkaListener(topics = "#{'${communi.business.message.topic}'}")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
//        如果信息为空 跳出

        if (kafkaMessage.isPresent()) {
            List<TaskInfo> taskInfoList = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            String messageGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoList));
//            一次发送的消息中 所有消息的groupId都是一样的 获取第一个的groupId
//            如果是对应的组别信息
            if (topicGroupId.equals(messageGroupId)) {
                pushService.push(taskInfoList);
            }
        }

    }
}
