package org.chenzc.communi.config;

import org.chenzc.communi.entity.TaskContextData;
import org.chenzc.communi.template.TaskController;
import org.chenzc.communi.template.TaskTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenz
 * @date 2024/05/21
 * 责任链配置类 所有的业务
 * 维护多个类
 */
@Configuration
public class ChainConfig {
//    private HashMap<String, TaskTemplate> taskTemplates;

    @Bean("TaskController")
    public TaskController taskController(){
        TaskController taskController = TaskController.builder().build();

        TaskTemplate<TaskContextData> taskTemplate = TaskTemplate.builder().taskTemplate(Arrays.asList()).build();
//     TODO 以后将对应责任链中的节点任务放入asList中

        Map<String,TaskTemplate<TaskContextData>> taskTemplates  = new HashMap<>();
        taskTemplates.put("test_task",taskTemplate);
        taskController.setTaskTemplates(taskTemplates);
        return taskController;
    }
}
