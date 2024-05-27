package org.chenzc.communi.push;


import org.chenzc.communi.entity.TaskInfo;

import java.util.List;

/**
 * 请求消息下发的接口
 * @author chenz
 * @date 2024/05/27
 */
public interface PushService {

    void push(List<TaskInfo> taskInfoList);
}
