package org.chenzc.communi.service.impl;

import org.chenzc.communi.entity.TaskContext;
import org.chenzc.communi.entity.TaskContextData;
import org.chenzc.communi.entity.send.SendRequest;
import org.chenzc.communi.entity.send.SendResponse;
import org.chenzc.communi.enums.BusinessEnums;
import org.chenzc.communi.service.SendService;
import org.chenzc.communi.task.SendContextData;
import org.chenzc.communi.template.TaskController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SendServiceImpl implements SendService {

    @Autowired
    @Qualifier("ServiceTaskController")
    private TaskController taskController;

    @Override
    public SendResponse sendMessage(SendRequest sendRequest) {

//        构造上下文数据
        SendContextData sendContextData = SendContextData.builder()
                .messageParam(Collections.singletonList(sendRequest.getMessageParam()))
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .build();

//        构造上下文
        TaskContext<TaskContextData> sendContext = TaskContext.builder()
                .businessCode(BusinessEnums.SINGLE_SEND.getCode())
                .businessType(BusinessEnums.SINGLE_SEND.getMessage())
                .businessContextData(sendContextData)
                .Exception(Boolean.FALSE)
                .build();

//        责任链执行任务
        TaskContext<TaskContextData> taskContext = taskController.executeChain(sendContext);

//        构造SendResponse返回响应结果
        return SendResponse.builder().code(taskContext.getResponse().getCode()).build();
    }

    @Override
    public SendResponse batchSendMessage(SendRequest sendRequest) {
        return null;
    }
}
