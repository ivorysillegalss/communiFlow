package org.chenzc.communi.task.deduplication.builder;

import cn.hutool.core.text.StrPool;
import org.chenzc.communi.constant.TaskConstant;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.DeduplicationType;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("FrequencyDeduplicationBuilder")
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

    /**
     * 业务规则去重 构建key
     * <p>
     * key ： receiver + sendChannel
     * <p>
     * 一天内一个用户只能收到某个渠道的消息 N 次
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    public String createSingleDeduplicationKey(TaskInfo taskInfo, String receiver) {
        return StringUtils.append(TaskConstant.SIMPLE_LIMIT_TAG_PREFIX
                , StrPool.C_UNDERLINE
                , receiver
                , StrPool.C_UNDERLINE
                , taskInfo.getSendChannel());
    }
}

