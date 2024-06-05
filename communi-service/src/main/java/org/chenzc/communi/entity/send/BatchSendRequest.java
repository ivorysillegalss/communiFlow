package org.chenzc.communi.entity.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.chenzc.communi.entity.messageTemplate.MessageParam;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchSendRequest {

    /**
     * 执行的业务类型
     * @see org.chenzc.communi.enums.BusinessEnums
     */
    private String code;


    /**
     * 消息模板的id
     */
    private Long messageTemplateId;


    /**
     * 消息相关参数
     */
    private List<MessageParam> messageParamList;

}
