package org.chenzc.communi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举类
 * @author chenz
 * @date 2024/05/29
 */
@Getter
@AllArgsConstructor
public enum RespEnums implements PowerfulEnums{


    SUCCESS("1","成功"),
    FAIL("0","失败"),
    CLIENT_BAD_VARIABLES("-1","参数解析错误"),
    CLIENT_BAD_PARAMETERS("2","含接受者的参数列表为空"),

    SEND_MQ_ERROR("3","发送MQ信息错误"),
    SEND_MSG_MQ_SUCCESS("1","消息成功下发至MQ"),

    /**
     * 消息丢弃
     */
    MESSAGE_DISCARD("4","消息模板被丢弃"),

    /**
     * 消息夜间屏蔽
     */
    MESSAGE_NIGHT_SHIELD("5","消息被夜间屏蔽"),

    MESSAGE_NIGHT_SHIELD_NEXT_DAY_SEND("6","消息被夜间屏蔽，次日发送"),

    NULL_LEGAL_RECEIVERS("4","没有合法的接受者"),

    /**
     * 消息处理
     */
    MESSAGE_HANDLE_SUCCESS("5","消息下发成功"),

    MESSAGE_HANDLE_ERROR("6","消息下发失败"),

    /**
     * 详见子类build方法
     */
    DEDUPLICATION_BUILD_ERR("5","去重对象构建失败"),

    CRON_TASK_SERVICE_ERROR("6","启动定时任务服务失败"),

    CRON_TASK_SAVE_ERROR("7","定时任务更新 or 保存失败");

    /**
     * 响应编码
     */
    private final String code;
    /**
     * 响应信息
     */
    private final String message;

}
