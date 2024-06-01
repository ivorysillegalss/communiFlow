package org.chenzc.communi.content;

import lombok.*;

/**
 * @author 3y
 * <p>
 * 短信内容模型
 * <p>
 * 在前端填写的时候分开，但最后处理的时候会将url拼接在content上
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsContentModel extends ContentModel {

    /**
     * 短信发送内容
     */
    private String content;

    /**
     * 短信发送链接 (短信中夹杂的链接 默认在短信之后发)
     */
    private String url;

}
