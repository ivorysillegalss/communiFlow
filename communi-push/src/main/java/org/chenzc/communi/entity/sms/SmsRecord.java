package org.chenzc.communi.entity.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信 发送 & 拉取回执
 *
 * @author chenz
 * @date 2024/06/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsRecord {
    private Long id;

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 渠道商Id
     */
    private Integer supplierId;

    /**
     * 渠道商名字
     */
    private String supplierName;

    /**
     * 短信发送的内容
     */
    private String msgContent;

    /**
     * 批次号Id
     */
    private String seriesId;

    /**
     * 计费条数
     */
    private Integer chargingNum;

    /**
     * 回执信息
     */
    private String reportContent;

    /**
     * 短信状态
     */
    private String status;

    /**
     * 发送日期
     */
    private Integer sendDate;

    /**
     * 创建时间
     */
    private Integer created;

    /**
     * 更新时间
     */
    private Integer updated;
}
