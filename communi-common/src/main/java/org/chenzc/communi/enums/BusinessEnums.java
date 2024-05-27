package org.chenzc.communi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessEnums {

//    TODO 增加业务枚举类
    SINGLE_SEND("100","单发"),
    BATCH_SEND("101","批量发送");

    /**
     * 响应编码
     */

    private final String code;
    /**
     * 响应信息
     */
    private final String message;
}
