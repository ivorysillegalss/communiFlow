package org.chenzc.communi.pending;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.config.CronAsyncThreadPoolConfig;
import org.chenzc.communi.constant.PendingConstant;
import org.chenzc.communi.entity.CrowdInfo;
import org.chenzc.communi.entity.messageTemplate.MessageParam;
import org.chenzc.communi.entity.send.BatchSendRequest;
import org.chenzc.communi.enums.BusinessEnums;
import org.chenzc.communi.service.SendService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 延迟批量处理人群信息
 *
 * @author chenz
 * @date 2024/06/04
 */
@Component
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//多例模式
public class CrowdBatchTaskPending extends AbstractLazyPending<CrowdInfo> {

    @Resource
    private SendService sendService;


    /**
     * 无参构造方法 为队列构建基本配置项
     */
    public CrowdBatchTaskPending() {
        this.pendingParam = PendingParam.<CrowdInfo>builder()
                .executorService(CronAsyncThreadPoolConfig.getConsumePendingThreadPool())
                .queue(new LinkedBlockingQueue<>(PendingConstant.QUEUE_SIZE))
                .numThreshold(PendingConstant.BATCH_RECEIVER_SIZE)
                .timeThreshold(PendingConstant.TIME_THRESHOLD)
                .build();
    }

    @Override
    public void doHandle(List<CrowdInfo> params) {

//        如果参数相同，组装成同一个MessageParam发送
        Map<Map<String, String>, Set<String>> paramMap = MapUtil.newHashMap();
        for (CrowdInfo crowdInfo : params) {
            String receiver = crowdInfo.getReceiver();
            Map<String, String> vars = crowdInfo.getParams();

            Set<String> setReceiver = org.chenzc.communi.utils.StringUtils.spilt(receiver, StrPool.COMMA);

            if (Objects.isNull(paramMap.get(vars))) {
                paramMap.put(vars, setReceiver);

            } else {
                paramMap.get(vars).addAll(setReceiver);
            }
        }

//        组装messageParam参数
        List<MessageParam> messageParams = Lists.newArrayList();
        for (Map.Entry<Map<String, String>, Set<String>> entry : paramMap.entrySet()) {
            MessageParam messageParam = MessageParam.builder()
                    .receiver(entry.getValue())
                    .variables(Collections.singletonList(entry.getKey()))
                    .build();
            messageParams.add(messageParam);
        }

//        组装发送参数
        BatchSendRequest batchSendRequest = BatchSendRequest.builder()
                .messageTemplateId(CollUtil.getFirst(params.listIterator()).getMessageTemplateId())
                .code(BusinessEnums.BATCH_SEND.getCode())
                .messageParamList(messageParams)
                .build();

        sendService.batchSendMessage(batchSendRequest);
    }

}
