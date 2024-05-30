package org.chenzc.communi.constant;

/**
 * 消息执行推送前业务 & 推送时所用常量
 *
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
     * 按频次去重的限流前缀
     */
    public static final String SIMPLE_LIMIT_TAG_PREFIX = "SIMPLE_LIMIT";
    /**
     * 去重次数累加
     */
    public static final Integer LIMIT_ACCUMULATE = 1;

//    /**
//     * 按频次去重的前缀
//     */
//    public static final String FREQUENCY_LIMIT_TAG = "FRE_";

    /**
     * 按时间去重的前缀
     */
    public static final String CONTENT_LIMIT_TAG_PREFIX = "SLIDE_WINDOW";

    /**
     * 存放限流脚本配置的文件路径
     */
    public static final String LIMIT_LUA_SCRIPT_PATH = "limit.lua";

    public static final Integer TO_MILLISECONDS = 1000;
}
