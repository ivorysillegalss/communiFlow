package org.chenzc.communi.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShieldType implements PowerfulEnums {

    /**
     * 不进行夜间消息的屏蔽 夜间正常下发信息
     */
    NOT_NIGHT_SHIELD("1","夜间不进行屏蔽"),

    /**
     * 过滤夜间发送的信息
     */
    NIGHT_SHIELD("2","夜间进行屏蔽"),

    /**
     * 夜间收到的信息下发请求 在第二天进行发送
     */
    NIGHT_SHIELD_NEXT_DAY_SEND("3","夜间进行屏蔽 但是次日发送");

    private final String code;
    private final String message;
}
