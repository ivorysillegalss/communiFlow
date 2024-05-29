package org.chenzc.communi.task.deduplication.builder;

import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.entity.DeduplicationEntity;

/**
 * 对象构建器
 *
 * @author chenz
 * @date 2024/05/28
 */
public interface DeduplicationBuilder {
    DeduplicationEntity build(String deduplicationConfig, TaskInfo taskInfo);
}
