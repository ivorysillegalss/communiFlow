package org.chenzc.communi.task;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.entity.TaskContext;
import org.chenzc.communi.entity.TaskContextResponse;
import org.chenzc.communi.entity.messageTemplate.MessageParam;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.executor.TaskNodeModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chenz
 * @date 2024/05/23
 */
@Slf4j
@Service
public class PreCheckTask implements TaskNodeModel<SendContextData> {

    /** 前置参数检查
     * @param taskContext
     */
    @Override
    public void execute(TaskContext<SendContextData> taskContext) {
        SendContextData contextData = taskContext.getBusinessContextData();
        Long messageTemplateId = contextData.getMessageTemplateId();
        List<MessageParam> messageParamList = contextData.getMessageParam();

//        过滤消息模板为null & 列表为null
        if (Objects.isNull(messageTemplateId) || CollUtil.isEmpty(messageParamList)) {
            taskContext.setException(Boolean.TRUE)
                    .setResponse(TaskContextResponse.<SendContextData>builder()
                            .code(RespEnums.CLIENT_BAD_PARAMETERS.getCode())
                            .build());
            return;
        }

//        过滤receiver=null
        List<MessageParam> resultMessageParamList = messageParamList.stream()
                .filter(messageParam -> !CollUtil.isEmpty(messageParam.getReceiver()))
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(resultMessageParamList)) {
            taskContext.setException(Boolean.TRUE)
                    .setResponse(TaskContextResponse.<SendContextData>builder()
                            .code(RespEnums.CLIENT_BAD_PARAMETERS.getCode())
                            .build());
            return;
        }

        taskContext.getBusinessContextData().setMessageParam(resultMessageParamList);
    }
}
