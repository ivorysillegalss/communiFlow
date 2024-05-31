package org.chenzc.communi.task;

import org.chenzc.communi.entity.TaskContext;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.executor.TaskNodeModel;
import org.chenzc.communi.handler.HandlerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 处理完其余逻辑 调用第三方服务下发消息
 * @author chenz
 * @date 2024/06/01
 */
@Service
public class SendMessageTask implements TaskNodeModel<TaskInfo> {

    @Resource
    private HandlerFactory handlerFactory;


    /**
     * 获取对应的handler处理器对象 根据不同的第三方渠道下方消息
     * @param taskContext 上下文
     */
    @Override
    public void execute(TaskContext<TaskInfo> taskContext) {
        TaskInfo taskInfo = taskContext.getBusinessContextData();
        handlerFactory.route(taskInfo.getSendChannel()).doHandler(taskInfo);
    }
}
