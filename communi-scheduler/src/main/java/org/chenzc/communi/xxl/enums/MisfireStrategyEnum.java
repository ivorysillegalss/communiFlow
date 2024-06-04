package org.chenzc.communi.xxl.enums;

/**
 * 调度过期策略
 *
 * @author chenzc
 * @date 2024/06/04
 */
public enum MisfireStrategyEnum {

    /**
     * do nothing
     */
    DO_NOTHING,

    /**
     * fire once now
     */
    FIRE_ONCE_NOW;

    MisfireStrategyEnum() {
    }
}
