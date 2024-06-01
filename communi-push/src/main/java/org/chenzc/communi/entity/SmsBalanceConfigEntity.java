package org.chenzc.communi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 负载均衡的配置实体类
 * 标记权重 渠道等信息
 * @author chenz
 * @date 2024/06/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsBalanceConfigEntity {

    /**
     * 负载均衡权重
     */
    private Integer weights;

    /**
     *短信模板 若有指定发送者 则字段有效
     */
    private Integer sendAccount;

    /**
     * 下发渠道 （第三方服务）
     * eg 华为云 腾讯云
     */
    private String scriptName;
}
