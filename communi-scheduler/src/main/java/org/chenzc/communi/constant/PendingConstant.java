
package org.chenzc.communi.constant;

/**
 * 延迟缓冲 pending 常量信息
 *
 * @author chenz
 * @date 2024/06/04
 */
public class PendingConstant {
    /**
     * 阻塞队列大小
     */
    public static final Integer QUEUE_SIZE = 100;
    /**
     * 触发执行的数量阈值
     */
    public static final Integer NUM_THRESHOLD = 100;
    /**
     * batch 触发执行的时间阈值，单位毫秒【必填】
     */
    public static final Long TIME_THRESHOLD = 1000L;

    /**
     * 接口限制 发送任务 接收者数量上限
     */
    public static final Integer BATCH_RECEIVER_SIZE = 100;

    /**
     * 夜间屏蔽次日发送的key
     */
    public static final String NIGHT_SHIELD_NEXT_DAY_SEND_KEY = "night_shield_send";

    private PendingConstant() {
    }


}
