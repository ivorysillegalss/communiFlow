package org.chenzc.communi.handler;

/**
 * 定时任务到店处理的处理器
 * @author chenz
 * @date 2024/06/04
 */

public interface TaskHandler {


    /**
     * 处理定时消息
     *
     * @param messageTemplateId 对应定时任务的消息模板
     */
    public void handle(Long messageTemplateId);

}
