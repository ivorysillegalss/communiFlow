package org.chenzc.communi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.print.attribute.IntegerSyntax;

/**
 * 短信状态信息
 *
 * @author chenzc
 * @date 2024/06/01
 */
@Getter
@ToString
@AllArgsConstructor
public enum SmsStatus implements PowerfulEnums {

    /**
     * 调用渠道接口发送成功
     */
    SEND_SUCCESS("10", "调用渠道接口发送成功"),
    /**
     * 用户收到短信(收到渠道短信回执，状态成功)
     */
    RECEIVE_SUCCESS("20", "用户收到短信(收到渠道短信回执，状态成功)"),
    /**
     * 用户收不到短信(收到渠道短信回执，状态失败)
     */
    RECEIVE_FAIL("30", "用户收不到短信(收到渠道短信回执，状态失败)"),
    /**
     * 调用渠道接口发送失败
     */
    SEND_FAIL("40", "调用渠道接口发送失败");

    private final String code;
    private final String message;
}
