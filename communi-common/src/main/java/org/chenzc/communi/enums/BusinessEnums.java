package org.chenzc.communi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务信息枚举类
 * @author chenz
 * @date 2024/05/29
 */
@Getter
@AllArgsConstructor
public enum BusinessEnums implements PowerfulEnums{

//    TODO 增加业务枚举类
    SINGLE_SEND("100","单发"),
    BATCH_SEND("101","批量发送"),

    PIPELINE_HANDLER("200","信息下发");

    /**
     * 响应编码
     */

    private final String code;
    /**
     * 响应信息
     */
    private final String message;
}
