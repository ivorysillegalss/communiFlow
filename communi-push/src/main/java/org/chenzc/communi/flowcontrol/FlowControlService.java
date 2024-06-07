package org.chenzc.communi.flowcontrol;

import org.chenzc.communi.entity.TaskInfo;

/**
 * 限流控制类
 *
 * @author chenz
 * @date 2024/06/06
 */
public interface FlowControlService {
    /**
     * 根据不同的渠道进行流量控制
     * （不同的FlowControlParam有不同的配置）
     *
     * @param taskInfo
     * @return {@link Double }
     */
    Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam);
}
