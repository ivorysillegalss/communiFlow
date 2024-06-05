package org.chenzc.communi.pending;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * pending 初始化参数配置类
 * @author chenz
 * @date 2024/06/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PendingParam<T> {

    /**
     * 消费线程池实例
     */
    protected ExecutorService executorService;

    /**
     * 阻塞队列
     */
    protected BlockingQueue<T> queue;

    /**
     * 触发执行的数量阈值
     */
    private Integer numThreshold;

    /**
     * 触发执行的时间阈值 单位 ms
     */
    private Long timeThreshold;

}
