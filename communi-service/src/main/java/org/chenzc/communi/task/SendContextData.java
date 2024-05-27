package org.chenzc.communi.task;

import lombok.*;
import org.chenzc.communi.entity.TaskContextData;
import org.chenzc.communi.entity.messageTemplate.MessageParam;

import java.util.List;

@Builder
@Getter
@Setter
public class SendContextData extends TaskContextData {
    /**
     * 消息模板的ID
     */
    private Long messageTemplateId;

    /**
     *消息参数
     */
    private List<MessageParam> messageParam;
}
