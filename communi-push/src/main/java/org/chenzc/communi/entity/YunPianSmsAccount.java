package org.chenzc.communi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 云片账号信息
 *
 * <p>
 * 账号参数示例：
 * <p>
 * {
 * "url":"https://sms.yunpian.com/v2/sms/tpl_batch_send.json",
 * "apikey":"caffff8234234231b5cd7",
 * "tpl_id":"523333332",
 * "supplierId":20,
 * "supplierName":"云片",
 * "scriptName":"YunPianSmsScript"
 * }
 *
 * @author chenzc
 * @date 2024/06/01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YunPianSmsAccount extends SmsAccount {

    /**
     * apikey
     */
    private String apikey;
    /**
     * tplId
     */
    private String tplId;

    /**
     * api相关
     */
    private String url;

}
