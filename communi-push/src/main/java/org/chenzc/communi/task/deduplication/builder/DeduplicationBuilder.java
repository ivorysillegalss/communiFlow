package org.chenzc.communi.task.deduplication.builder;

import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.task.deduplication.service.AbstractDeduplicationService;

import java.util.List;

/**
 * 对象构建器
 *
 * @author chenz
 * @date 2024/05/28
 */
public interface DeduplicationBuilder {
    /**
     * 构建实体去重规则
     * @param deduplicationConfig json字符串形式规则
     * @param taskInfo 任务信息
     * @return {@link DeduplicationConfigEntity }
     */
    DeduplicationConfigEntity build(String deduplicationConfig, TaskInfo taskInfo);

    String createSingleDeduplicationKey(AbstractDeduplicationService service,TaskInfo taskInfo,String receiver);
    List<String> createDeduplicationKey(AbstractDeduplicationService service, TaskInfo taskInfo);
}
