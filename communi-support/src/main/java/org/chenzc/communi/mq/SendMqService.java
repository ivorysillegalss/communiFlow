package org.chenzc.communi.mq;

import org.springframework.stereotype.Service;

/**
 * mq发送信息
 *
 * @author chenz
 * @date 2024/05/24
 */

@Service
public interface SendMqService {

    /**
     * 发送信息
     */
    void send(String topicId,String tagId,String jsonTaskInfos) throws Exception;

}
