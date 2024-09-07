package org.chenzc.communi.receiver;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.push.PushService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "communi.mq.pipeline", havingValue = CommonConstant.RABBIT_MQ)
public class RabbitmqReceiver {
    public static final String MSG_TYPE_SEND = "send";
    public static final String MSG_TYPE_RECALL = "recall";

    @Resource
    private PushService pushService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.queues}", durable = "true"),
            exchange = @Exchange(value = "${communi.rabbitmq.exchange.name}", type = ExchangeTypes.TOPIC),
            key = "${communi.rabbitmq.routing.key}"
    ))
    public void onMessage(Message message) {
        String messageType = message.getMessageProperties().getHeader("messageType");
        String messageContent = new String(message.getBody());
        if (StringUtils.isBlank(messageContent)) {
            return;
        }
        if (MSG_TYPE_SEND.equals(messageType)) {
            List<TaskInfo> taskInfos = JSON.parseArray(messageContent, TaskInfo.class);
            pushService.push(taskInfos);
        }
    }
}
