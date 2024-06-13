package org.chenzc.communi.service;

import org.chenzc.communi.entity.MessageTemplate;
import org.chenzc.communi.entity.trace.MessageInfo;

/**
 *数据链路追踪 总业务类
 * @author chenz
 * @date 2024/06/13
 */
public interface DataService {
    /**
     * 根据消息id获取消息详细信息
     * @param messageId 消息id
     * @return {@link MessageInfo }
     */
    MessageInfo getMessageInfoById(String messageId);


    /**
     * 根据接收者维度来获取
     * @param receiver
     * @return {@link MessageInfo }
     */
    MessageInfo getTraceUserInfo(String receiver);


    /**
     * 根据id获取消息模板的相关详细信息
     * @param businessId
     * @return {@link MessageTemplate }
     */
    MessageTemplate getTraceMessageTemplateInfo(String businessId);
}
