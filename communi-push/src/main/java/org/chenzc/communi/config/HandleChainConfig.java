package org.chenzc.communi.config;

import org.chenzc.communi.enums.BusinessEnums;
import org.chenzc.communi.task.DeduplicationTask;
import org.chenzc.communi.task.DiscardTask;
import org.chenzc.communi.task.NightShieldTask;
import org.chenzc.communi.template.TaskController;
import org.chenzc.communi.template.TaskTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;


/**
 * 责任链配置 消费层调用api前所需执行的业务 去重
 *
 * @author chenz
 * @date 2024/05/27
 */
@Configuration
public class HandleChainConfig {

    @Resource
    private DiscardTask discardTask;

    @Resource
    private NightShieldTask nightShieldTask;

    @Resource
    private DeduplicationTask deduplicationTask;

    @Bean("handleTemplate")
    public TaskTemplate handleTemplate() {
        return TaskTemplate.builder()
                .taskTemplate(Arrays.asList(discardTask, nightShieldTask, deduplicationTask))
                .build();
    }

    @Bean("HandleTaskController")
    public TaskController taskController() {
        TaskController taskController = TaskController.builder().build();
        HashMap<String, TaskTemplate> taskTemplates = new HashMap<>();
        taskTemplates.put(BusinessEnums.PIPELINE_HANDLER.getCode(), handleTemplate());
        taskController.setTaskTemplates(taskTemplates);
        return taskController;
    }
}
