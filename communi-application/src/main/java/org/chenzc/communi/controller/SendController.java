package org.chenzc.communi.controller;

import org.chenzc.communi.entity.send.SendRequest;
import org.chenzc.communi.entity.send.SendResponse;
import org.chenzc.communi.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    private SendService sendService;

    /**
     *  发送信息
     * @param sendRequest 消息请求体 包含对应的消息模板 消息接收者etc
     * @return {@link SendResponse }
     */
    @PostMapping("/send")
    public SendResponse sendMessage(@RequestBody SendRequest sendRequest){
        return sendService.sendMessage(sendRequest);
    }
}
