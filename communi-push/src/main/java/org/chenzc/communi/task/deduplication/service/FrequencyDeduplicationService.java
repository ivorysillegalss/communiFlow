package org.chenzc.communi.task.deduplication.service;

import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.builder.DeduplicationBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 根据消息频次去重 实现类
 * @author chenz
 * @date 2024/05/30
 */
@Service("FrequencyDeduplicationService")
public class FrequencyDeduplicationService extends AbstractDeduplicationService implements DeduplicationService {
    @Resource
    @Qualifier("FrequencyDeduplicationBuilder")
    private DeduplicationBuilder deduplicationBuilder;


    @Override
    public String createSingleDeduplicationKey(TaskInfo taskInfo, String receiver) {
        return deduplicationBuilder.createSingleDeduplicationKey(taskInfo, receiver);
    }
}
