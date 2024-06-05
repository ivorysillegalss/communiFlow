package org.chenzc.communi.config;

import cn.hutool.core.thread.ExecutorBuilder;
import com.dtp.common.em.QueueTypeEnum;
import com.dtp.common.em.RejectedTypeEnum;
import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.thread.ThreadPoolBuilder;
import org.chenzc.communi.constant.CronConstant;
import org.chenzc.communi.constant.ThreadPoolConstant;
import org.chenzc.communi.xxl.constant.XxlJobConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池动态配置 （热更新）
 * 配置优先级：Nacos （远端）> Local
 *
 * @author chenz
 * @date 2024/06/04
 */
public class CronAsyncThreadPoolConfig {

    private CronAsyncThreadPoolConfig() {

    }


    /**
     * 业务：消费pending队列实际的线程池
     * 配置：核心线程 可被回收，当线程池 无被引用 且无 核心线程数 时 应当被回收
     * @return {@link ExecutorService }
     */
    public static ExecutorService getConsumePendingThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(ThreadPoolConstant.COMMON_CORE_POOL_SIZE)
                .setMaxPoolSize(ThreadPoolConstant.COMMON_MAX_POOL_SIZE)
                .setWorkQueue(new LinkedBlockingQueue<>(ThreadPoolConstant.BIG_QUEUE_SIZE))
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
//                设置拒绝策略为 当线程池中任务已满时 将由提交任务的线程继续执行
                .setAllowCoreThreadTimeOut(true)
                .setKeepAliveTime(ThreadPoolConstant.SMALL_KEEP_LIVE_TIME, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 业务：接收到xxl-job请求的线程池
     * 配置：不丢弃信息，核心线程数不会被回收
     * @return {@link DtpExecutor }
     */
    public static DtpExecutor getXxlCronExecutor(){
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName(CronConstant.EXECUTE_XXL_THREAD_NAME)
                .corePoolSize(ThreadPoolConstant.COMMON_CORE_POOL_SIZE)
                .maximumPoolSize(ThreadPoolConstant.COMMON_MAX_POOL_SIZE)
                .keepAliveTime(ThreadPoolConstant.COMMON_KEEP_LIVE_TIME)
                .timeUnit(TimeUnit.SECONDS)
                .rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
                .allowCoreThreadTimeOut(false)
                .workQueue(QueueTypeEnum.VARIABLE_LINKED_BLOCKING_QUEUE.getName(), ThreadPoolConstant.COMMON_QUEUE_SIZE, false)
                .buildDynamic();
    }
}
