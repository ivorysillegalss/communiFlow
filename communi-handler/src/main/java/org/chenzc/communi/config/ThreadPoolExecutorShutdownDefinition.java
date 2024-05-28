package org.chenzc.communi.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 包装线程池优雅关闭相关
 *
 * @author chenz
 * @date 2024/05/27
 */
@Component
public class ThreadPoolExecutorShutdownDefinition implements ApplicationListener<ContextClosedEvent> {

    private final List<ExecutorService> POOLS = Collections.synchronizedList(new ArrayList<>(12));

    //    设置过期时间
    private static final Long AWAIT_TERMINATION = 20L;

    //    设置过期时间单位为秒
    public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    //    将对应的线程池假如线程池的池子中
    public void registryExecutor(ExecutorService executorService) {
        POOLS.add(executorService);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
//        如果池子为空 则直接退出
        if (CollectionUtils.isEmpty(POOLS)) {
            return;
        }
//        线程池在调用shutdown之后默认不会接受新的任务 但是会继续完成剩下的任务后关闭
        for (ExecutorService pool : POOLS) {
            pool.shutdown();
            try {
//                如果超过了限定的时间还有任务没完成 则写入警告日志
                if (!pool.awaitTermination(AWAIT_TERMINATION, TIME_UNIT)) {
//                    处理消息 补充日志等 TBD TODO
                }
//                遇到错误同理
            } catch (InterruptedException ex) {

                Thread.currentThread().interrupt();
            }
        }

    }
}
