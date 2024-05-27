package org.chenzc.communi.entity.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.chenzc.communi.entity.messageTemplate.MessageParam;

/**
 * 发送请求时的主体类
 * @author chenz
 * @date 2024/05/22
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class SendRequest {
    /**
     * 消息模板的ID
     */
    private Long messageTemplateId;

    /**
     * 业务类型 （send or recall etc.）
     */
    private String businessType;

    /**
     * 消息包含的内容 （占位符 & 接受者等信息）
     */
    private MessageParam messageParam;
}
