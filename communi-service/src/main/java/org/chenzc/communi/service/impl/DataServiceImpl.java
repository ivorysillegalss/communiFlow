package org.chenzc.communi.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.system.UserInfo;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.dao.MessageTemplateDao;
import org.chenzc.communi.entity.MessageTemplate;
import org.chenzc.communi.entity.SimpleTrackEventEntity;
import org.chenzc.communi.entity.trace.MessageInfo;
import org.chenzc.communi.entity.trace.TraceResponse;
import org.chenzc.communi.enums.ChannelType;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.service.DataService;
import org.chenzc.communi.service.TraceService;
import org.chenzc.communi.utils.EnumsUtils;
import org.chenzc.communi.utils.RedisUtils;
import org.chenzc.communi.utils.TaskInfoUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 数据消息追踪获取接口 实现类
 * 此处对应communi-stream模块下的清理操作
 *
 * @author chenz
 * @date 2024/06/13
 * @see org.chenzc.communi.sink.RedisSink
 */
@Service
@Slf4j
public class DataServiceImpl implements DataService {

    @Resource
    private MessageTemplateDao messageTemplateDao;

    @Resource
    private TraceService traceService;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public MessageInfo getMessageInfoById(String messageId) {
        TraceResponse traceResponse = traceService.traceByMessageId(messageId);
        if (CollUtil.isEmpty(traceResponse.getData())) {
            log.warn("can't find the message according to the id");
            return null;
        }
        Long businessId = CollUtil.getFirst(traceResponse.getData().iterator()).getBusinessId();
        Long messageTemplateId = TaskInfoUtils.getMessageTemplateIdFromBusinessId(businessId);
        MessageTemplate messageTemplate = messageTemplateDao.selectById(messageTemplateId);
        return MessageInfo.builder()
                .businessId(businessId)
                .creator(messageTemplate.getCreator())
                .sendType(EnumsUtils.getEnumByCode(messageTemplate.getSendChannel(), ChannelType.class).getMessage())
                .title(messageTemplate.getName())
                .build();
    }

    @Override
    public MessageInfo getTraceUserInfo(String receiver) {
        List<String> userInfoList = redisUtils.lRange(receiver, 0, -1);
        if (CollUtil.isEmpty(userInfoList)) {
            log.warn("can't find the message according to the receiverId");
            return null;
        }

        ArrayList<SimpleTrackEventEntity> simpleTrackEventEntities = Lists.newArrayList();
        for (String userInfo : userInfoList) {
            SimpleTrackEventEntity info = JSON.parseObject(userInfo, SimpleTrackEventEntity.class);
            simpleTrackEventEntities.add(info);
        }

        Long businessId = CollUtil.getFirst(simpleTrackEventEntities.iterator()).getBusinessId();
        Long messageTemplateId = TaskInfoUtils.getMessageTemplateIdFromBusinessId(businessId);
        MessageTemplate messageTemplate = messageTemplateDao.selectById(messageTemplateId);
        return MessageInfo.builder()
                .businessId(businessId)
                .creator(messageTemplate.getCreator())
                .sendType(EnumsUtils.getEnumByCode(messageTemplate.getSendChannel(), ChannelType.class).getMessage())
                .title(messageTemplate.getName())
                .build();
    }

    @Override
    public MessageTemplate getTraceMessageTemplateInfo(String businessIdOrTemplateId) {
//        传进来的可能是模板id或是businessId 通过长度进行分辨
        String businessId;
        if (!(CommonConstant.BUSINESS_ID_LENGTH == businessIdOrTemplateId.length())) {
            MessageTemplate messageTemplate = messageTemplateDao.selectById(businessIdOrTemplateId);
            businessId = String.valueOf(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()));
        } else businessId = businessIdOrTemplateId;
        MessageTemplate messageTemplate = messageTemplateDao.selectById(TaskInfoUtils.getMessageTemplateIdFromBusinessId(Long.valueOf(businessId)));
        if (Objects.isNull(messageTemplate)){
            log.warn("get messageTemplate wrong");
            return null;
        }
//        获取sink清洗后的数据
//        Map<Object, Object> cleanResult = redisUtils.hGetAll(businessId);
        return messageTemplate;
    }
}
