package org.chenzc.communi.task;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.dao.MessageTemplateDao;
import org.chenzc.communi.entity.TaskContext;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.entity.messageTemplate.MessageParam;
import org.chenzc.communi.entity.MessageTemplate;
import org.chenzc.communi.executor.TaskNodeModel;
import org.chenzc.communi.utils.TaskInfoUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenz
 * @date 2024/05/23
 */
@Slf4j
@Service
public class AssembleInfoTask implements TaskNodeModel<SendContextData> {

    @Resource
    private MessageTemplateDao messageTemplateDao;

    /**
     * 组装对应的taskinfo信息
     *
     * @param taskContext 上下文数据
     */
    @Override
    public void execute(TaskContext<SendContextData> taskContext) {
        List<MessageParam> messageParams = taskContext.getBusinessContextData().getMessageParam();
        MessageTemplate messageTemplate = messageTemplateDao.selectById(taskContext.getBusinessContextData().getMessageTemplateId());

        List<TaskInfo> taskInfoList = assembleTask(messageTemplate, messageParams);
        taskContext.getBusinessContextData().setTaskInfos(taskInfoList);
    }

    public List<TaskInfo> assembleTask(MessageTemplate messageTemplate, List<MessageParam> messageParams) {
        List<TaskInfo> taskInfoList = new ArrayList<>();
        for (MessageParam messageParam : messageParams) {
            TaskInfo taskInfo = TaskInfo.builder()
                    .receiver(messageParam.getReceiver())
                    .messageTemplateId(messageTemplate.getId())
                    .sendAccount(messageTemplate.getSendAccount())
                    .sendChannel(messageTemplate.getSendChannel())
                    .shieldType(messageTemplate.getShieldType())
                    .templateType(messageTemplate.getTemplateType())
                    .msgType(messageTemplate.getMsgType())
                    .idType(messageTemplate.getIdType())
                    .businessId(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .build();
            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }
}
