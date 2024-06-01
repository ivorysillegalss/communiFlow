package org.chenzc.communi.script;

import org.chenzc.communi.entity.SmsParam;
import org.chenzc.communi.entity.SmsRecord;

import java.util.List;

/**
 * 短信脚本接口 不同提供sms服务的运营商执行的父接口
 * 获取实例 & 执行时使用
 *
 * @author chenz
 * @date 2024/06/01
 */
public interface SmsScript {

    /**
     * 发送短信
     * @param smsParam 短信参数
     * @return {@link List }<{@link SmsRecord }>
     */
    List<SmsRecord> send(SmsParam smsParam);
}
