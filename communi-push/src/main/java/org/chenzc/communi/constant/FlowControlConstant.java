package org.chenzc.communi.constant;

/**
 * 限流相关常量
 * @author chenz
 * @date 2024/06/06
 */
public class FlowControlConstant {
    public static final String FLOW_CONTROL_KEY = "flowControlRule";
    public static final String FLOW_CONTROL_PREFIX = "flow_control_";

    public static final Integer DEFAULT_PERMITS = 1;

    /**
     * 邮件限流数
     */
    private final Double DEFAULT_EMAIL_LIMIT = 3D;
}
