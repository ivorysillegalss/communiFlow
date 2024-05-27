package org.chenzc.communi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RespEnums implements PowerfulEnums{


    SUCCESS("1","成功"),
    FAIL("0","失败"),
    CLIENT_BAD_PARAMETERS("2","含接受者的参数列表为空"),
    SEND_MQ_ERROR("3","发送MQ信息错误"),
    SEND_MSG_MQ_SUCCESS("1","消息成功下发至MQ");

    /**
     * 响应编码
     */
    private final String code;
    /**
     * 响应信息
     */
    private final String message;

}
