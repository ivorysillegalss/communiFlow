package org.chenzc.communi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsAccount {


    /**
     * 第三方服务ID
     */
    private Integer supplierId;


    /**
     * 第三方渠道商名字
     */
    private String supplierName;

    /**
     * 账号配置
     */
    private String scriptName;
}
