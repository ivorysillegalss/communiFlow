package org.chenzc.communi.constant;

/**
 * 消息执行推送前业务 所用常量
 *
 * @author chenz
 * @date 2024/05/29
 */
public class TaskConstant {

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


    /**
     * 运行lua脚本所需单位换算
     */
    public static final Integer TO_MILLISECONDS = 1000;


    /**
     * 丢弃的消息模板的ID
     */
    public static final String DISCARD_MESSAGE_TEMPLATE_ID = "discardMessageTemplateId";

    /**
     * 存储需要进行夜间消息屏蔽的ID
     */
    public static final String NIGHT_SHIELD_TEMPLATE_ID = "nightShieldTemplateId";

    /**
     * 消息夜间屏蔽所用
     * <p>
     * 配置晚上具体时间 （超过此时间需要将信息放到第二天早上再发）
     */
    public static final Integer NIGHT_TIME = 9;

    /**
     * 夜间消息屏蔽 第二天再发送
     * 先将消息暂存到redis中 此为redis前缀
     *
     * @see org.chenzc.communi.task.NightShieldTask
     */
    public static final String NIGHT_SHIELD_NEXT_DAY_SEND_KEY = "nightShieldNextDaySend";

    /**
     * 一天 = N 秒
     */
    public static final Long SECONDS_OF_A_DAY = 86400L;
}
