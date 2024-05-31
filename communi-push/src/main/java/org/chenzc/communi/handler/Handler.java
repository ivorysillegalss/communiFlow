package org.chenzc.communi.handler;


import org.chenzc.communi.entity.TaskInfo;

/**
 *  消息处理器 （真正在各种任务之后 调用三方api下发信息的地方）
 * @author chenz
 * @date 2024/05/27
 */
public interface Handler {

    /**
     * 下发消息
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

}
