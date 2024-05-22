package org.chenzc.communi.executor;

import org.chenzc.communi.entity.TaskContext;
import org.chenzc.communi.entity.TaskContextData;

/**
 * @author chenz
 * @date 2024/05/21
 * 任何责任链中的节点都需要实现该接口
 */
public interface TaskNodeModel<T extends TaskContextData> {
    void execute(TaskContext<T> taskContext);

}