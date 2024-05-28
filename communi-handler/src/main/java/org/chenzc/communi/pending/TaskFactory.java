package org.chenzc.communi.pending;

import com.dtp.core.thread.DtpExecutor;
import org.chenzc.communi.config.TaskHandlerThreadPoolConfig;
import org.chenzc.communi.utils.GroupIdMappingUtils;
import org.chenzc.communi.utils.ThreadPoolUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 初始化给所有的业务类型 分配对应的线程池
 * 并且提供对应的路由方法
 *
 * @author chenz
 * @date 2024/05/27
 */
@Component
public class TaskFactory {

    @Resource
    private ThreadPoolUtils threadPoolUtils;

    private static final List<String> groupsId = GroupIdMappingUtils.getAllGroupIds();

    private Map<String, ExecutorService> TaskPendingMap;

    /**
     * 声明周期方法
     * 注册优雅关闭 & 存放入保存池的Map中
     */
    @PostConstruct
    public void init() {
        TaskPendingMap = new HashMap<>();

        for (String groupId : groupsId) {
            DtpExecutor executor = TaskHandlerThreadPoolConfig.getExecutor(groupId);

//            注册优雅关闭
            threadPoolUtils.elegantShutdownRegistry(executor);

//            存放入map当中
            TaskPendingMap.put(groupId, executor);

        }
    }

    public ExecutorService route(String groupId) {
        return TaskPendingMap.get(groupId);
    }
}
