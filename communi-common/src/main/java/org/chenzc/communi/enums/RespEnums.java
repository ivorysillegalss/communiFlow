package org.chenzc.communi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RespEnums {


    SUCCESS("0","成功"),
    FAIL("-1","失败");

    /**
     * 响应编码
     */
    private final String code;
    /**
     * 响应信息
     */
    private final String message;

}
