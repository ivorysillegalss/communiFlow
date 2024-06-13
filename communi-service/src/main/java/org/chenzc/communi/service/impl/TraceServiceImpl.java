package org.chenzc.communi.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.entity.SimpleTrackEventEntity;
import org.chenzc.communi.entity.trace.TraceResponse;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.service.TraceService;
import org.chenzc.communi.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TraceServiceImpl implements TraceService {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public TraceResponse traceByMessageId(String messageId) {
        if (CharSequenceUtil.isBlank(messageId)) {
            return TraceResponse.builder().code(RespEnums.CLIENT_BAD_VARIABLES.getCode()).message(RespEnums.CLIENT_BAD_VARIABLES.getMessage()).build();
        }

        String redisMessageKey = CharSequenceUtil.join(StrUtil.COLON, CommonConstant.CACHE_KEY_PREFIX, CommonConstant.CACHE_MESSAGE_ID, messageId);
        List<String> messageList = redisUtils.lRange(redisMessageKey, 0, -1);

        if (CollUtil.isEmpty(messageList)){
            return TraceResponse.builder().code(RespEnums.CLIENT_BAD_VARIABLES.getCode()).message(RespEnums.CLIENT_BAD_VARIABLES.getMessage()).build();
        }

        ArrayList<SimpleTrackEventEntity> trackEventEntities = Lists.newArrayList();
        for (String message : messageList) {
            trackEventEntities.add(JSON.parseObject(message, SimpleTrackEventEntity.class));
        }
        return TraceResponse.builder().data(trackEventEntities).build();
    }
}
