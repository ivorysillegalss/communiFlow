package org.chenzc.communi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chenzc.communi.content.ContentModel;

import java.io.Serializable;
import java.util.Set;

/**
 * 发送任务信息
 *
 * @author chenzc
 * @date 2024/05/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo extends TaskContextData implements Serializable {

    /**
     * 业务消息发送Id, 用于链路追踪, 若不存在, 则使用 messageId
     */
    private String bizId;

    /**
     * 消息唯一Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     */
    private String messageId;

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 业务Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     */
//    生成逻辑上可以做文章 TODO
    private Long businessId;

    /**
     * 接收者
     */
    private Set<String> receiver;

    /**
     * 发送的Id类型
     */
    private Integer idType;

    /**
     * 发送渠道
     */
    private String sendChannel;

    /**
     * 模板类型
     */
    private Integer templateType;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 屏蔽类型
     */
    private String shieldType;

    /**
     * 发送文案模型
     * message_template表存储的content是JSON(所有内容都会塞进去)
     * 不同的渠道要发送的内容不一样(比如发push会有img，而短信没有)
     * 所以会有ContentModel
     */
//    未补充第三方接入模型内容 TODO

    private ContentModel contentModel;

    /**
     * 发送账号（邮件下可有多个发送账号、短信可有多个发送账号..）
     */
    private Integer sendAccount;


}
