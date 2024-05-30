package org.chenzc.communi.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 发送ID类型枚举
 *
 * @author chenzc
 * @date 2024/05/29
 */
@Getter
@ToString
@AllArgsConstructor
public enum IdType implements PowerfulEnums {
    /**
     * 站内userId
     */
    USER_ID("10", "userId"),
    /**
     * 手机设备号
     */
    DID("20", "did"),
    /**
     * 手机号
     */
    PHONE("30", "phone"),
    /**
     * 邮件
     */
    EMAIL("50", "email"),
    ;

    private final String code;
    private final String message;

}
