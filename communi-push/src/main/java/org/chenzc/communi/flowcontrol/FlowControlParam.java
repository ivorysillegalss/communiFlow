package org.chenzc.communi.flowcontrol;


import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chenzc.communi.enums.RateLimitStrategy;

/**
 * 限流规则实体类
 *
 * @author chenz
 * @date 2024/06/06
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowControlParam {

    /**
     * 限流器
     * 子类初始化的指定
     */
    protected RateLimiter rateLimiter;

    /**
     * 限流器所配置的大小 （由初始化的时候子类指定）
     */
    protected Double rateInitValue;

    /**
     * 所指定的限流策略
     */
    protected RateLimitStrategy rateLimitStrategy;
}
