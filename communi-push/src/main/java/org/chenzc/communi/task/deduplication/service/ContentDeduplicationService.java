package org.chenzc.communi.task.deduplication.service;

import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.builder.DeduplicationBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("ContentDeduplicationService")
public class ContentDeduplicationService extends AbstractDeduplicationService implements DeduplicationService {

    @Resource
    @Qualifier("ContentDeduplicationBuilder")
    private DeduplicationBuilder deduplicationBuilder;

    @Override
    public String createSingleDeduplicationKey(TaskInfo taskInfo, String receiver) {
        return deduplicationBuilder.createSingleDeduplicationKey(taskInfo, receiver);
    }
}
