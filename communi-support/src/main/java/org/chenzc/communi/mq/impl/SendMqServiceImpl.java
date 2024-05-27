package org.chenzc.communi.mq.impl;


import org.chenzc.communi.mq.SendMqService;
import org.springframework.stereotype.Service;

/**
 * 具体mq发送消息的实现类 默认使用kafka
 * @author chenz
 * @date 2024/05/24
 */
@Service
public class SendMqServiceImpl implements SendMqService {

    @Override
    public void send(String topicId, String tagId, String jsonTaskInfos) {
        System.out.println("1232454567887665442");
    }
}
