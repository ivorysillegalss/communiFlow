package org.chenzc.communi.flowcontrol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.constant.FlowControlConstant;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.ChannelType;
import org.chenzc.communi.enums.RateLimitStrategy;
import org.chenzc.communi.flowcontrol.annotation.SimpleRateLimit;
import org.chenzc.communi.service.ConfigService;
import org.chenzc.communi.utils.EnumsUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流配置工厂类
 *
 * @author chenz
 * @date 2024/06/06
 */
@Service
@Slf4j
public class FlowControlFactory implements ApplicationContextAware {

    private final Map<RateLimitStrategy, FlowControlService> flowControlRateLimitStrategy = new ConcurrentHashMap<>();

    @Resource
    private ConfigService configService;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 利用common保重的限流算法达成限流（令牌桶）
     * @param taskInfo
     * @param flowControlParam
     */
    public void doFlowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter;
        Double rateInitValue = flowControlParam.getRateInitValue();
        Double rateLimitConfig = getRateLimitConfig(taskInfo.getSendChannel());

        if (Objects.nonNull(rateLimitConfig) && !rateInitValue.equals(rateLimitConfig)){
            rateLimiter = RateLimiter.create(rateLimitConfig);
            flowControlParam.setRateLimiter(rateLimiter);
            flowControlParam.setRateInitValue(rateLimitConfig);
        }

        FlowControlService flowControlService = flowControlRateLimitStrategy.get(flowControlParam.getRateLimitStrategy());
        if (Objects.isNull(flowControlService)){
            log.error("not found the rateLimitStrategy");
            return;
        }

//        记录执行时间等
        Double costTime = flowControlService.flowControl(taskInfo, flowControlParam);
        if (costTime > 0){
            log.info("consumer {} flow control time {}",
                    EnumsUtils.getEnumByCode(taskInfo.getSendChannel(), ChannelType.class).getMessage(), costTime);
        }
    }

    /**
     * 获取限流的相关配置
     *
     * @param channelCode
     * @return {@link Double }
     */
    private Double getRateLimitConfig(String channelCode) {
        String property = configService.getProperty(FlowControlConstant.FLOW_CONTROL_KEY, CommonConstant.EMPTY_JSON_OBJECT);
        JSONObject jsonObject = JSON.parseObject(property);

//        判空
        if (Objects.isNull(jsonObject.getDouble(StringUtils.join(FlowControlConstant.FLOW_CONTROL_PREFIX, channelCode)))) {
            return null;
        }

        return jsonObject.getDouble(StringUtils.join(FlowControlConstant.FLOW_CONTROL_PREFIX, channelCode));
    }

    /**
     * 初始化方法 将限流相关的服务类放进工厂当中
     */
    @PostConstruct
    private void init() {
        Map<String, Object> serviceMap = this.applicationContext.getBeansWithAnnotation(SimpleRateLimit.class);
        serviceMap.forEach((name, service) -> {
            if (service instanceof FlowControlService) {
                SimpleRateLimit simpleRateLimit = AopUtils.getTargetClass(service).getAnnotation(SimpleRateLimit.class);
                RateLimitStrategy rateLimitStrategy = simpleRateLimit.rateLimitStrategy();
                flowControlRateLimitStrategy.put(rateLimitStrategy, (FlowControlService) service);
            }
        });
    }

}
