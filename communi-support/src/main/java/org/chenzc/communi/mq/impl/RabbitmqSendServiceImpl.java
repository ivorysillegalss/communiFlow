package org.chenzc.communi.mq.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.mq.SendMqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author chenz
 * @date 2024/09/07
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "communi.mq.pipeline", havingValue = CommonConstant.RABBIT_MQ)
public class RabbitmqSendServiceImpl implements SendMqService {


    @Resource
    private RabbitTemplate rabbitTemplate;


    @Value("${communi.rabbitmq.topic.name}")
    private String confTopic;

    @Value("${communi.rabbitmq.exchange.name}")
    private String exchangeName;

    @Override
    public void send(String topic, String jsonValue) throws Exception {
        if (topic.equals(confTopic)) {
            rabbitTemplate.convertAndSend(exchangeName, confTopic, jsonValue);
        } else {
            log.error("RabbitSendMqServiceImpl send topic error! topic:{},confTopic:{}", topic, confTopic);
        }
    }
}
