package org.chenzc.communi.task.deduplication.builder;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import org.chenzc.communi.constant.PushConstant;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.DeduplicationType;
import org.chenzc.communi.enums.TrackEventType;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("ContentDeduplicationBuilder")
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements DeduplicationBuilder {


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

    /**
     * 利用md5创建去重key
     *
     * @param taskInfo 任务信息
     * @return {@link List }<{@link String }>
     */
    @Override
    public String createSingleDeduplicationKey(TaskInfo taskInfo, String receiver) {
        String hexStr = DigestUtil.md5Hex(StringUtils.append(String.valueOf(taskInfo.getMessageTemplateId()),
                receiver, JSON.toJSONString(taskInfo.getContentModel())));
        return StringUtils.append(PushConstant.CONTENT_LIMIT_TAG_PREFIX, hexStr);
    }
}
