package org.chenzc.communi.task.deduplication.builder;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.DeduplicationType;
import org.chenzc.communi.enums.TrackEventType;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.task.deduplication.service.AbstractDeduplicationService;
import org.chenzc.communi.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements DeduplicationBuilder{


    public ContentDeduplicationBuilder() {
        this.deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationConfigEntity build(String deduplicationConfig, TaskInfo taskInfo) {
        DeduplicationConfigEntity entity = parseEntityFromConfig(deduplicationType, deduplicationConfig, taskInfo);
        if (Objects.isNull(entity)) {
            return null;
        }

        return entity.setEventType(TrackEventType.DEDUPLICATION_CONTENT);
    }
}
