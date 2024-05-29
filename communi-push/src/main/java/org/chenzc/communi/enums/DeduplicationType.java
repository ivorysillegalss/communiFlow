package org.chenzc.communi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeduplicationType implements PowerfulEnums {

    /**
     * 相同内容去重
     */
    CONTENT("10", "相同内容去重"),

    /**
     *频次消息去重
     */
    FREQUENCY("20", "一天内N次相同内容去重");

    private final String code;
    private final String message;
}
