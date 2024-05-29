package org.chenzc.communi.task.deduplication.limit;

import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.task.deduplication.service.AbstractDeduplicationService;

import java.util.Set;

/**
 * 存放去重逻辑的接口
 *
 * @author chenz
 * @date 2024/05/28
 */
public interface LimitDeduplicationService {
    Set<String> doLimit(AbstractDeduplicationService service, DeduplicationConfigEntity entity);
}
