package org.chenzc.communi.pending;

import jakarta.annotation.Resource;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.chenzc.communi.entity.TaskContext;
import org.chenzc.communi.entity.TaskContextData;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.BusinessEnums;
import org.chenzc.communi.template.TaskController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * 多线程执行任务
 *
 * @author chenz
 * @date 2024/05/27
 */
@Data
@Accessors(chain = true)
@Builder
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component

public class Task implements Runnable {

    private TaskInfo taskInfo;

    @Resource
    @Qualifier("HandleTaskController")
//TODO    为什么这个地方命名没有跟 communi-push 模块产生连接 但是可以注入bean？？？？？？？
    private TaskController taskController;

    //    执行责任链的任务
    @Override
    public void run() {

//        执行任务
        TaskContext<TaskContextData> taskContext = TaskContext.builder()
                .businessContextData(taskInfo)
                .businessType(BusinessEnums.PIPELINE_HANDLER.getCode()).build();

//        责任链启动
        taskController.executeChain(taskContext);
    }
}
