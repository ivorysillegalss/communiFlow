package org.chenzc.communi.service;

import org.chenzc.communi.entity.trace.TraceResponse;

/**
 * 消息真实查询的接口
 * @author chenz
 * @date 2024/06/13
 */
public interface TraceService {
    /**
     * 基于消息id 查询对应的链路结果
     * @param messageId
     * @return {@link TraceResponse }
     */
    TraceResponse traceByMessageId(String messageId);
}
