package org.chenzc.communi.mq.impl;


import org.chenzc.communi.mq.SendMqService;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import javax.annotation.Resource;

/**
 * 具体mq发送消息的实现类 默认使用kafka
 * @author chenz
 * @date 2024/05/24
 */
@Service
public class SendMqServiceImpl implements SendMqService {

    @Resource
    private KafkaTemplate kafkaTemplate;

    /**
     * kafka消息发送 此处仅发送消息
     * 没有做其他的处理 & 优化
     * @param topicId
     * @param jsonValue
     */

//    可进一步优化为多topic 每一topic对应一渠道 目前处理为一topic多groupId 所有信息通过判断是否自己对应消费组来决定是否消费 TODO
    @Override
    public void send(String topicId, String jsonValue) {
        kafkaTemplate.send(topicId,jsonValue);
    }
}
