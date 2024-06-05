package org.chenzc.communi.config;

import cn.hutool.core.thread.ExecutorBuilder;
import org.chenzc.communi.constant.ThreadPoolConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * pending缓冲队列 线程池配置类
 *
 * @author chenz
 * @date 2024/06/04
 */
public class PendingThreadPoolConfig {

    private PendingThreadPoolConfig() {

    }

    /**
     * 业务：应用于pending队列的单线程池
     * 配置：核心线程可以被回收，当线程池无被引用且无核心线程数时 应被回收
     * @return {@link ExecutorService }
     */
    public static ExecutorService getPendingSingleThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(ThreadPoolConstant.SINGLE_CORE_POOL_SIZE)
                .setMaxPoolSize(ThreadPoolConstant.SINGLE_MAX_POOL_SIZE)
                .setWorkQueue(new LinkedBlockingQueue<>(ThreadPoolConstant.BIG_QUEUE_SIZE))
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setAllowCoreThreadTimeOut(true)
                .setKeepAliveTime(ThreadPoolConstant.SMALL_KEEP_LIVE_TIME, TimeUnit.SECONDS)
                .build();
    }

}
