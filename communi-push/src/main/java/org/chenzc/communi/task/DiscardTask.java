package org.chenzc.communi.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.constant.PushConstant;
import org.chenzc.communi.entity.*;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.enums.TrackEventType;
import org.chenzc.communi.executor.TaskNodeModel;
import org.chenzc.communi.service.ConfigService;
import org.chenzc.communi.utils.LogUtils;
import org.chenzc.communi.utils.TaskInfoUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 消息丢弃业务责任链节点 判断消息对应模板是否合法（被丢弃）
 * @author chenz
 * @date 2024/05/31
 */
@Service
public class DiscardTask implements TaskNodeModel<TaskInfo> {

    @Resource
    private ConfigService config;

    @Resource
    private LogUtils logUtils;

    /**
     *         如果能在对应的配置文件中的被丢弃的消息模板列表中 找到该消息模板
     *         则记录日志 并丢弃此消息 写入日志
     * @param taskContext 上下文
     */
    @Override
    public void execute(TaskContext<TaskInfo> taskContext) {
        TaskInfo taskInfo = taskContext.getBusinessContextData();
        String discardMessageTemplateIdsArray = config.getProperty(PushConstant.DISCARD_MESSAGE_TEMPLATE_ID, CommonConstant.EMPTY_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(discardMessageTemplateIdsArray);

        if (jsonArray.contains(String.valueOf(taskInfo.getMessageTemplateId()))){

            logUtils.print(
                    LogEntity.builder().build()
                    ,
                    TrackEventEntity.builder()
                            .state(TrackEventType.DISCARD.getCode())
                            .timestamp(System.currentTimeMillis())
                            .businessId(TaskInfoUtils.generateBusinessId(taskInfo.getMessageTemplateId(), taskInfo.getTemplateType()))
                            .messageId(TaskInfoUtils.generateMessageId())
                            .receivers(taskInfo.getReceiver())
                            .bizId(taskInfo.getBizId())
                            .build()
            );

            taskContext.setException(Boolean.TRUE)
                    .setResponse(TaskContextResponse.<TaskInfo>builder()
                            .code(RespEnums.MESSAGE_DISCARD.getCode())
                            .build());
        }

    }
}
