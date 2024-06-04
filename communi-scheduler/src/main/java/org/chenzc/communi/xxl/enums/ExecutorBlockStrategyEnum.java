package org.chenzc.communi.xxl.enums;

/**
 * 执行阻塞队列
 *
 * @author chenzc
 * @date 2024/06/04
 */
public enum ExecutorBlockStrategyEnum {
    /**
     * 单机串行
     */
    SERIAL_EXECUTION,

    /**
     * 丢弃后续调度
     */
    DISCARD_LATER,

    /**
     * 覆盖之前调度
     */
    COVER_EARLY;

    ExecutorBlockStrategyEnum() {

    }
}
