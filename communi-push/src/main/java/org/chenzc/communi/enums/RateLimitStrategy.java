package org.chenzc.communi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 限流的枚举类
 * @author chenz
 * @date 2024/06/06
 */
@Getter
@ToString
@AllArgsConstructor
public enum RateLimitStrategy implements PowerfulEnums{

    /**
     * 根据真实请求数限流 (实际意义上的QPS）
     */
    REQUEST_RATE_LIMIT("10", "根据真实请求数限流"),
    /**
     * 根据发送用户数限流（人数限流）
     */
    SEND_USER_NUM_RATE_LIMIT("20", "根据发送用户数限流"),
    ;

    private final String code;
    private final String message;


}
