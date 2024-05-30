package org.chenzc.communi.task;

import cn.hutool.core.collection.CollUtil;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.constant.PushConstant;
import org.chenzc.communi.entity.TaskContext;
import org.chenzc.communi.entity.TaskContextResponse;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.DeduplicationType;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.executor.TaskNodeModel;
import org.chenzc.communi.service.ConfigService;
import org.chenzc.communi.task.deduplication.DeduplicationFactory;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.utils.EnumsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeduplicationTask implements TaskNodeModel<TaskInfo> {

    @Resource
    private DeduplicationFactory factory;

    @Resource
    private ConfigService configService;

    @Override
    public void execute(TaskContext<TaskInfo> taskContext) {
        TaskInfo taskInfo = taskContext.getBusinessContextData();

//        从配置文件中读取去重规则 如果没有对应的规则则返回默认值
        String deduplicationConfig = configService.getProperty(PushConstant.DEDUPLICATION_RULE_KEY, CommonConstant.EMPTY_JSON_OBJECT);

//        先从配置中获取到所有的去重配置
        List<String> deduplicationList = EnumsUtil.getCodeList(DeduplicationType.class);
        for (String deduplicationType : deduplicationList) {
            DeduplicationConfigEntity entity = factory.newBuilderInstance(deduplicationType).build(deduplicationConfig, taskInfo);

//            异常处理
//            如果在构建去重参数的时候遇到错误 或者经过去重后接收者为空 则报错返回
            if (entity.getException()) {
                taskContext.setException(Boolean.TRUE)
                        .setResponse(TaskContextResponse.<TaskInfo>builder()
                                .code(RespEnums.DEDUPLICATION_BUILD_ERR.getCode())
                                .build());
                return;
            }

            factory.newServiceInstance(deduplicationType).deduplication(entity);

            if (CollUtil.isEmpty(taskInfo.getReceiver())) {
                taskContext.setException(Boolean.TRUE)
                        .setResponse(TaskContextResponse.<TaskInfo>builder()
                                .code(RespEnums.NULL_LEGAL_RECEIVERS.getCode())
                                .build());
            }
        }
    }
}
