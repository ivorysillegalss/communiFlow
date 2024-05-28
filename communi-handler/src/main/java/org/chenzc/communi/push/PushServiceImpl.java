package org.chenzc.communi.push;

import cn.hutool.core.collection.CollUtil;
import org.chenzc.communi.pending.Task;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.pending.TaskFactory;
import org.chenzc.communi.utils.GroupIdMappingUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PushServiceImpl implements PushService {

    @Resource
    private TaskFactory taskFactory;

    /**
     * 推送消息 （具体业务未补充）
     *
     * @param taskInfoList
     */


//    这个方法看着流程很简单 实则是消费曾的核心逻辑所在
//    传过来的taskInfoList中 都是同一个topic的 消费者组ID相同

//    本项目的架构是 根据不同的信息业务类型 分配线程池 （不同的业务类型通过不同线程池进行处理）
//    也就是说 在获取到groupId的同时 就知道要存进哪一个线程池进行处理了

//    构建一个Task对象 表示多线程长所需执行的任务 （仅包装不运行 其其中任务流程同样通过责任链模式配置）
//    通过存储线程池信息的map 路由到对应的线程池 并执行对应的任务
    @Override
    public void push(List<TaskInfo> taskInfoList) {
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoList.listIterator()));
        for (TaskInfo taskInfo : taskInfoList) {
            Task task = Task.builder().taskInfo(taskInfo).build();
            taskFactory.route(topicGroupId).execute(task);
        }
    }
}
