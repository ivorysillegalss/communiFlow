package org.chenzc.communi.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.RateLimitStrategy;
import org.chenzc.communi.flowcontrol.FlowControlParam;
import org.chenzc.communi.flowcontrol.FlowControlService;
import org.chenzc.communi.flowcontrol.annotation.SimpleRateLimit;

/**
 * 根据请求的用户的数量来改变
 *
 * @author chenz
 * @date 2024/06/06
 */

@SimpleRateLimit(rateLimitStrategy = RateLimitStrategy.SEND_USER_NUM_RATE_LIMIT)
public class SendUserRateLimitStrategy implements FlowControlService {
    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo
     * @param flowControlParam
     * @return {@link Double }
     */
    @Override
    public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(taskInfo.getReceiver().size());
    }
}
