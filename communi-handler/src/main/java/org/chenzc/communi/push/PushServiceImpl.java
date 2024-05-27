package org.chenzc.communi.push;

import cn.hutool.core.collection.CollUtil;
import org.chenzc.communi.pending.Task;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.utils.GroupIdMappingUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushServiceImpl implements PushService{
    @Override
    public void push(List<TaskInfo> taskInfoList) {
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoList.listIterator()));
        for (TaskInfo taskInfo : taskInfoList) {
            Task task = Task.builder().taskInfo(taskInfo).build();
            task.run();
        }
    }
}
