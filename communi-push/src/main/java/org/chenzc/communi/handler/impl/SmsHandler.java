package org.chenzc.communi.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.annonation.Handler;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.constant.HandlerConstant;
import org.chenzc.communi.content.SmsContentModel;
import org.chenzc.communi.entity.SmsAccount;
import org.chenzc.communi.entity.SmsBalanceConfigEntity;
import org.chenzc.communi.entity.SmsParam;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.ChannelType;
import org.chenzc.communi.handler.BaseHandler;
import org.chenzc.communi.service.ConfigService;
import org.chenzc.communi.utils.StringUtils;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Handler
@Slf4j
public class SmsHandler extends BaseHandler {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private ConfigService configService;

    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();

//        限流配置TBD TODO
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
//        封装对象 关于运营商和发送账号配置在下文负载均衡中配置
        SmsParam smsParam = SmsParam.builder()
                .phone(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .build();

        try {
//            1. 得到负载均衡的权重配置
//           TODO 或许可以解耦配置sendAccount和负载均衡?
            List<SmsBalanceConfigEntity> smsConfig = getSmsConfig(taskInfo);
//            2. 执行负载均衡 返回数组得到对应的 首选 & 备选对象
//            TODO 设计多种负载均衡的策略or算法 使其可配置化
            SmsBalanceConfigEntity[] balanceEntities = loadBalance(smsConfig);

            for (SmsBalanceConfigEntity balanceEntity : balanceEntities) {
                smsParam.setScriptName(balanceEntity.getScriptName())
                        .setSendAccountId(balanceEntity.getSendAccount());

                applicationContext.getBean(balanceEntity.getScriptName(),SmsSc)
            }

        } catch (Exception e) {
            log.error("SmsHandler#handler fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
        }
        return false;
    }

    private SmsBalanceConfigEntity[] loadBalance(List<SmsBalanceConfigEntity> smsConfig) {

    }

    /**
     * 获取短信渠道的业务信息
     *
     * @param taskInfo
     * @return {@link String }
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (CharSequenceUtil.isNotBlank(smsContentModel.getUrl())) {
            return StringUtils.append(smsContentModel.getContent(), CharSequenceUtil.SPACE, smsContentModel.getUrl());
        } else {
            return smsContentModel.getContent();
        }
    }

    /**
     * 根据消息模板判断需执行的配置 （负载均衡策略等）
     *
     * @param taskInfo
     * @return {@link List }<{@link SmsBalanceConfigEntity }>
     */
    private List<SmsBalanceConfigEntity> getSmsConfig(TaskInfo taskInfo) {

//        若指定发送账号（账号配置中有渠道相关信息） 则指定发送的渠道等
        if (!taskInfo.getSendAccount().equals(HandlerConstant.SMS_AUTO_FLOW_RULE)) {
//            CRUD TODO
            SmsAccount smsAccount = new SmsAccount();
            return Collections.singletonList(SmsBalanceConfigEntity.builder()
                    .scriptName(smsAccount.getScriptName())
                    .sendAccount(taskInfo.getSendAccount())
                    .weights(HandlerConstant.MAX_WEIGHTS)
                    .build());
        }

        String loadBalanceProperty = configService.getProperty(HandlerConstant.SMS_LOAD_BALANCE_CONFIG, CommonConstant.EMPTY_VALUE_JSON_ARRAY);

//        TODO 写法可优化
        JSONArray jsonArray = JSON.parseArray(loadBalanceProperty);
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonArray.getJSONObject(i).getJSONArray(StringUtils.append(HandlerConstant.SMS_LOAD_BALANCE_KEY, taskInfo.getMsgType()));
            if (CollUtil.isEmpty(jsonArray)) {
                return JSON.parseArray(JSON.toJSONString(jsonArray), SmsBalanceConfigEntity.class)
            }
        }

        log.error("SmsHandler#handler fail,Can find the config");
        return new ArrayList<>();
    }
}
