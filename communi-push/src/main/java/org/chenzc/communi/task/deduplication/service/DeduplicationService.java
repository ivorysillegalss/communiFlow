package org.chenzc.communi.task.deduplication.service;

import org.chenzc.communi.task.deduplication.entity.DeduplicationEntity;

/**
 * 执行的去重服务
 * @author chenz
 * @date 2024/05/28
 */
public interface DeduplicationService {
    void deduplication(DeduplicationEntity entity);
}
