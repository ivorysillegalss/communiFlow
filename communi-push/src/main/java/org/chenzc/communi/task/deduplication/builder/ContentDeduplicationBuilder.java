package org.chenzc.communi.task.deduplication.builder;

import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.DeduplicationType;
import org.chenzc.communi.enums.TrackEventType;
import org.chenzc.communi.task.deduplication.entity.DeduplicationEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements DeduplicationBuilder{


    public ContentDeduplicationBuilder() {
        this.deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationEntity build(String deduplicationConfig, TaskInfo taskInfo) {
        DeduplicationEntity entity = parseEntityFromConfig(deduplicationType, deduplicationConfig, taskInfo);
        if (Objects.isNull(entity)) {
            return null;
        }

        return entity.setEventType(TrackEventType.DEDUPLICATION_CONTENT);
    }
}
