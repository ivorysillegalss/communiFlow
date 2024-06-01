package org.chenzc.communi.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 发送消息参数
 * @author chenz
 * @date 2024/06/01
 */
@Data
@Builder
@Accessors(chain = true)
public class SmsParam {

    /**
     * 消息模板id
     */
    private Long messageTemplateId;

    /**
     * 需要发送的手机号
     */
    private Set<String> phone;

    /**
     * 发送账号的id
     */
    private Integer sendAccountId;

    /**
     * 渠道账号的脚本标识
     */
    private String scriptName;

    /**
     * 发送的文案
     */
    private String content;
}
