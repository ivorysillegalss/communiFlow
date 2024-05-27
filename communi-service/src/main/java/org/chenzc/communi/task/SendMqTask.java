package org.chenzc.communi.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.entity.TaskContext;
import org.chenzc.communi.entity.TaskContextResponse;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.executor.TaskNodeModel;
import org.chenzc.communi.mq.SendMqService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 消息下发MQ任务
 *
 * @author chenz
 * @date 2024/05/24
 */
@Service
@Slf4j
public class SendMqTask implements TaskNodeModel<SendContextData> {

    @Resource
    private SendMqService sendMqService;

    @Value("chenzc")
    private String tagId;

    @Value("chenzcTopic")
    private String topicId;

    @Override
    public void execute(TaskContext<SendContextData> taskContext) {
//        从上下文中取出数据 序列化包装发送
        List<TaskInfo> taskInfos = taskContext.getBusinessContextData().getTaskInfos();
        String jsonTaskInfos = JSON.toJSONString(taskInfos, SerializerFeature.WriteClassName);
//        下发信息 具体下发逻辑封装在support对应包中
        try {
            sendMqService.send(topicId, tagId, jsonTaskInfos);
        } catch (Exception e) {
            taskContext.setException(Boolean.TRUE).setResponse(TaskContextResponse.<SendContextData>builder()
                    .code(RespEnums.SEND_MQ_ERROR.getCode()).build());
        }
//        成功下发信息
        taskContext.setResponse(TaskContextResponse.<SendContextData>builder()
                .code(RespEnums.SEND_MSG_MQ_SUCCESS.getCode()).build());
    }
}
