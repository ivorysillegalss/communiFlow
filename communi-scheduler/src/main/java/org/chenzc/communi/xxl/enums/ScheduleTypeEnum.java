package org.chenzc.communi.xxl.enums;

/**
 * 调度类型
 * @author chenz
 * @date 2024/06/04
 */
public enum ScheduleTypeEnum {

    /**
     * 不按照定时 启动任务
     */
    NONE,

    /**
     * 根据 cron表达式 启动任务
     */
    CRON,

    /**
     * 表示以固定的时间间隔 启动调度任务
     * eg 当fix_rate的值为1 则说明每隔1s启动调度任务
     */
    FIX_RATE;

    ScheduleTypeEnum(){

    }
}
