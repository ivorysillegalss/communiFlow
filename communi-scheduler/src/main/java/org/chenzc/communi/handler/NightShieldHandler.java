package org.chenzc.communi.handler;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.config.PendingThreadPoolConfig;
import org.chenzc.communi.constant.PendingConstant;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 夜间屏蔽的延迟处理类
 * <p></>
 * 消息下发的时候 若晓溪类型是夜间屏蔽且此时为夜间
 * 则将消息push进redis中
 * 第二天早上执行此定时任务
 *
 * @author chenz
 * @date 2024/06/06
 */
@Service
@Slf4j
public class NightShieldHandler {
    @Resource
    private KafkaTemplate kafkaTemplate;

    @Value("${communi.business.message.topic}")
    private String topicName;

    @Resource
    private RedisUtils redisUtils;

    @XxlJob("nightShieldLazyJob")
    public void execute() {
        log.info("NightShieldLazyPendingHandler#execute!");
        PendingThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (redisUtils.lLen(PendingConstant.NIGHT_SHIELD_NEXT_DAY_SEND_KEY) > 0) {
                String taskInfo = redisUtils.lPop(PendingConstant.NIGHT_SHIELD_NEXT_DAY_SEND_KEY);

                if (CharSequenceUtil.isNotBlank(taskInfo)) {
                    try {
                        kafkaTemplate.send(topicName,
                                JSON.toJSONString(Collections.singletonList(JSON.parseObject(taskInfo, TaskInfo.class))),
                                SerializerFeature.WriteClassName);
                    } catch (Exception e) {
                        log.error("nightShieldLazyJob send kafka fail! e:{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
                    }
                }
            }
        });
    }
}