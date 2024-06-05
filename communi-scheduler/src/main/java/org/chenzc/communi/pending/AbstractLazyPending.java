package org.chenzc.communi.pending;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.config.PendingThreadPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 延迟消费 以生产者消费者实现阻塞队列
 *
 * @author chenz
 * @date 2024/06/04
 */
@Slf4j
@Data
public abstract class AbstractLazyPending<T> {

    /**
     * 子类构造方法必须初始化此参数 （配置延迟队列参数）
     */
    protected PendingParam<T> pendingParam;

    /**
     * 批量装载任务
     */
    private List<T> tasks = new ArrayList<>();

    /**
     * 上次执行的时间
     */
    private Long lastHandleTime = System.currentTimeMillis();

    /**
     * 是否终止线程 volatile型变量
     */
    private volatile Boolean stop = false;


    /**
     * 初始化 （消费阻塞队列的数据）
     */
    @PostConstruct
    public void initConsumePending() {
//        初始化配置线程池
        ExecutorService executorService = PendingThreadPoolConfig.getPendingSingleThreadPool();
        executorService.execute(() -> {
            while (true) {
                try {
//                    从队列中获取元素 存放入task暂存队列中
                    T value = pendingParam.getQueue().poll(pendingParam.getTimeThreshold(), TimeUnit.MICROSECONDS);
                    if (Objects.nonNull(value)) {
                        tasks.add(value);
                    }

//                    判断是否需要停止当前线程
                    if (Boolean.TRUE.equals(stop) && CollUtil.isNotEmpty(tasks)) {
                        executorService.shutdown();
//                        对应具体实现子类(CrowdTaskInfo)中的onComplete方法 停止将其读取
                        break;
                    }

//                    当任务的时间超限制 or 数量超限制的时候 开始执行消费任务
//                    时间超限制指的是  此次处理的时间（消费的时间 超过最大限制 需要重置task集合 并异步开始执行任务）
//                    数量超限制 则是超过了某一段时间中规定的最大处理任务的数量
                    if (CollUtil.isNotEmpty(tasks) && dataReady()) {
                        List<T> taskRef = tasks;

//                        更新任务队列的值 和 最后更新的时间 以便进行下一轮的消费
                        tasks = Lists.newArrayList();
                        lastHandleTime = System.currentTimeMillis();

                        pendingParam.getExecutorService().execute(() -> this.handle(taskRef));
                    }

                } catch (Exception e) {
                    log.error("Pending#initConsumePending failed:{}", Throwables.getStackTraceAsString(e));
                    Thread.currentThread().interrupt();
                }
            }
        });

    }


    /**
     * 时间超限 or 任务数量超限的时候
     * 说明已经可以异步开始执行任务了
     *
     * @return boolean
     */
    private boolean dataReady() {
        return tasks.size() >= pendingParam.getTimeThreshold() ||
                (System.currentTimeMillis() - lastHandleTime >= pendingParam.getTimeThreshold());
    }


    /**
     * 将元素放入阻塞队列中
     * @param t 被放入的元素
     */
    public void pending(T t) {
        try {
            pendingParam.getQueue().put(t);
        } catch (InterruptedException e) {
            log.error("Pending#pending error:{}", Throwables.getStackTraceAsString(e));
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 处理元素 （下发定时任务的真实信息）
     */
    public void handle(List<T> t) {
        if (CollUtil.isEmpty(t)) {
            return;
        }
        try {
            doHandle(t);
        } catch (Exception e) {
            log.error("Pending#handle failed:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 具体的元素处理的抽象方法 具体的是心啊根据子类不同的应用场景而定
     *
     * @param params 需要处理的元素
     */
    public abstract void doHandle(List<T> params);
}
