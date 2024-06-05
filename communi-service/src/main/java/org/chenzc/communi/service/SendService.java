package org.chenzc.communi.service;


import org.chenzc.communi.entity.send.BatchSendRequest;
import org.chenzc.communi.entity.send.SendRequest;
import org.chenzc.communi.entity.send.SendResponse;

/**
 * 下发消息的接口
 * @author chenz
 * @date 2024/05/22
 */
public interface SendService {

    /**
     * 下发消息至MQ （单发）
     * @param sendRequest 发送请求参数
     * @return {@link SendResponse }
     */
    SendResponse sendMessage(SendRequest sendRequest);


    /**
     * 批量下发消息至MQ
     * @param sendRequest 发送请求参数
     * @return {@link SendResponse }
     */
    SendResponse batchSendMessage(BatchSendRequest sendRequest);
}
