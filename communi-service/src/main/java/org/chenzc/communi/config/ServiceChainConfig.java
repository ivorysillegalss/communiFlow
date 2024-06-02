package org.chenzc.communi.config;

import jakarta.annotation.Resource;
import org.chenzc.communi.enums.BusinessEnums;
import org.chenzc.communi.task.AfterCheckTask;
import org.chenzc.communi.task.AssembleInfoTask;
import org.chenzc.communi.task.PreCheckTask;
import org.chenzc.communi.task.SendMqTask;
import org.chenzc.communi.template.TaskController;
import org.chenzc.communi.template.TaskTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @author chenz
 * @date 2024/05/21
 * 责任链配置类 接入层所有的业务配置
 * 维护多个类
 */
@Configuration
public class ServiceChainConfig {

//    下方四个是具体的接入层业务
    @Resource
    private PreCheckTask preCheckTask;
    @Resource
    private AssembleInfoTask assembleInfoTask;
    @Resource
    private AfterCheckTask afterCheckTask;
    @Resource
    private SendMqTask sendMqTask;

    @Bean("sendTemplate")
    public TaskTemplate sendTemplate() {
        return TaskTemplate.builder()
                .taskTemplate(Arrays.asList(preCheckTask, assembleInfoTask, afterCheckTask,sendMqTask))
                .build();
    }

    @Bean("ServiceTaskController")
    public TaskController taskController() {
        TaskController taskController = TaskController.builder().build();
        Map<String, TaskTemplate> taskTemplates = new HashMap<>();
//        由于设计原因 不同的链子（不同的TaskTemplate中） 所对应的上下文数据类型不相同
        taskTemplates.put(BusinessEnums.SINGLE_SEND.getCode(), sendTemplate());
        taskController.setTaskTemplates(taskTemplates);
        return taskController;
    }
}
