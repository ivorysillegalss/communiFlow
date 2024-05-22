package org.chenzc.communi.template;


import lombok.Builder;
import org.chenzc.communi.entity.TaskContextData;
import org.chenzc.communi.executor.TaskNodeModel;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author chenz
 * @date 2024/05/21
 * @Usage 工厂类方法 将不同的业务（节点）连起来形成链子
 */

@Builder
public class TaskTemplate<T extends TaskContextData> {
    private List<TaskNodeModel<T>> taskTemplate;

    public TaskTemplate() {
        taskTemplate = new ArrayList<>();
    }


    /**
     * @param taskNodeModel 新增节点
     *                      往元素中新增节点
     */
    public void put(TaskNodeModel<T> taskNodeModel) {
        taskTemplate.add(taskNodeModel);
    }

    /**
     * @param taskNodeModels 新增节点
     *                       新增多个节点
     */
    public void putAll(Collection<TaskNodeModel<T>> taskNodeModels) {
        taskTemplate.addAll(taskNodeModels);
    }

    public List<TaskNodeModel<T>> get(){
        return taskTemplate;
    }
}
