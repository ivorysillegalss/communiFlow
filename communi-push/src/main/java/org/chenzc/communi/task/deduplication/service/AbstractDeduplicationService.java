package org.chenzc.communi.task.deduplication.service;

import cn.hutool.core.collection.CollUtil;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.task.deduplication.limit.LimitDeduplicationService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 去重服务抽象类 规定去重相同逻辑 及 创建key方法
 * @author chenz
 * @date 2024/05/30
 */
public abstract class AbstractDeduplicationService implements DeduplicationService {

    @Resource
    private LimitDeduplicationService limitService;

    @Override
    public void deduplication(DeduplicationConfigEntity entity) {
        Set<String> limitReceiver = limitService.doLimit(this, entity);
        Set<String> receivers = entity.getTaskInfo().getReceiver();

//        执行去重逻辑后获取到应当被筛掉的人
//        筛掉他
        if (!CollUtil.isEmpty(limitReceiver)) {
            receivers.removeAll(limitReceiver);
        }

    }

    @Override
    public List<String> createDeduplicationKey(AbstractDeduplicationService service,TaskInfo taskInfo) {
        Set<String> receivers = taskInfo.getReceiver();
        List<String> receiverKeys = new ArrayList<>(receivers.size());
        for (String receiver : receivers) {
            receiverKeys.add(service.createSingleDeduplicationKey(taskInfo,receiver));
        }
        return receiverKeys;
    }
}
