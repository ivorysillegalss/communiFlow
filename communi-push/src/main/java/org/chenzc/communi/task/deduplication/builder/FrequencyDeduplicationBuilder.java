package org.chenzc.communi.task.deduplication.builder;

import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.DeduplicationType;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder implements DeduplicationBuilder {
    public FrequencyDeduplicationBuilder() {
        this.deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationConfigEntity build(String deduplicationConfig, TaskInfo taskInfo) {
        DeduplicationConfigEntity entity = parseEntityFromConfig(deduplicationType, deduplicationConfig, taskInfo);
        if (Objects.isNull(entity)) return null;

        return entity.setTaskInfo(taskInfo);
    }
}
