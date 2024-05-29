package org.chenzc.communi.task.deduplication.service;

import cn.hutool.core.collection.CollUtil;
import org.chenzc.communi.task.deduplication.entity.DeduplicationEntity;
import org.chenzc.communi.task.deduplication.limit.LimitDeduplicationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

public abstract class AbstractDeduplicationService implements DeduplicationService {

    @Resource
    private LimitDeduplicationService limitService;

    @Override
    public void deduplication(DeduplicationEntity entity) {
        Set<String> limitReceiver = limitService.doLimit(this, entity);
        Set<String> receivers = entity.getTaskInfo().getReceiver();

//        执行去重逻辑后获取到应当被筛掉的人
//        筛掉他
        if (!CollUtil.isEmpty(limitReceiver)) {
            receivers.removeAll(limitReceiver);
        }

    }
}
