package org.chenzc.communi.constant;

/**
 * 消息执行推送前业务 & 推送时所用常量
 * @author chenz
 * @date 2024/05/29
 */
public class PushConstant {

    /**
     * 去重业务存到配指文件中的key
     */
    public static final String DEDUPLICATION_RULE_KEY = "deduplication";
    public static final String DEDUPLICATION_CONFIG_PREFIX = "deduplication_";


    /**
     * 限流前缀
     */
    public static final String LIMIT_TAG_PREFIX = "SIMPLE_LIMIT";
    /**
     * 去重次数累加
     */
    public static final Integer LIMIT_ACCUMULATE = 1;
}
