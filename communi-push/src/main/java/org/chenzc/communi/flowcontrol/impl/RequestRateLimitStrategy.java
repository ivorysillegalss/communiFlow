package org.chenzc.communi.flowcontrol.impl;


import com.google.common.util.concurrent.RateLimiter;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.RateLimitStrategy;
import org.chenzc.communi.flowcontrol.FlowControlParam;
import org.chenzc.communi.flowcontrol.FlowControlService;
import org.chenzc.communi.flowcontrol.annotation.SimpleRateLimit;

import static org.chenzc.communi.constant.FlowControlConstant.DEFAULT_PERMITS;

/**
 * 根据请求数进行限流 （QPS）
 *
 * @author chenz
 * @date 2024/06/06
 */
@SimpleRateLimit(rateLimitStrategy = RateLimitStrategy.REQUEST_RATE_LIMIT)
public class RequestRateLimitStrategy implements FlowControlService {

    @Override
    public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(DEFAULT_PERMITS);
    }
}
