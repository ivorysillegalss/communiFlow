package org.chenzc.communi.constant;


/**
 * 推送时第三方平台涉及到的常量
 *
 * @author chenz
 * @date 2024/06/01
 */
public class HandlerConstant {
    /**
     * 邮箱端点
     */
    public static final String EMAIL_END_POINT = "mail.smtp.ssl.socketFactory";
    public static final String URL_DATA_PATH = "users/communi/file/temp";


    /**
     * sms配置 如果非此值 则执行负载均衡
     * <p></p>
     * 若为此值 则说明指定了发送的渠道 视模板配置而定
     */
    public static final Integer SMS_AUTO_FLOW_RULE = 0;
    public static final Integer MAX_WEIGHTS = 100;
    public static final String SMS_LOAD_BALANCE_CONFIG = "smsHandleConfig";
    public static final String SMS_LOAD_BALANCE_KEY = "sms_handle_Config";

    public static final String PARAMS_SPLIT_KEY = "{|}";
    public static final String PARAMS_KV_SPLIT_KEY = "{:}";
    public static final Integer YUN_PIAN_SMS_DEFAULT_LIMIT = 2;
    public static final String SMS_API_KEY = "apikey";
    public static final String SMS_API_MOBILE = "mobile";
    public static final String SMS_TPL_ID = "tpl_id";
    public static final String SMS_TPL_VALUE = "tpl_value";
    public static final Integer SMS_DEFAULT_CAPACITY = 8;
    /**
     * 默认请求超时时间 单位为ms
     */
    public static final Integer SMS_DEFAULT_TIME_OUT = 2000;
}
