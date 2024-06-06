package org.chenzc.communi.sink;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import io.lettuce.core.RedisFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.chenzc.communi.entity.SimpleTrackEventEntity;
import org.chenzc.communi.entity.TrackEventEntity;
import org.chenzc.communi.utils.LettuceRedisUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.chenzc.communi.constant.FlinkConstant.MONTH;

/**
 * sink第三部分 信息持久化的步骤
 * 实时数据写入redis中
 * 分为两个层面进行记录 （用户 & 消息模板）
 * 1. 单用户单天收到的信息 数量级大 保留1天
 * 2. 消息模板整体下发 数量级小 保留30天
 *
 * @author chenz
 * @date 2024/06/06
 */
@Slf4j
public class RedisSink implements SinkFunction<TrackEventEntity> {

    /**
     * 此处集成了方法 调用回调函数
     * 其接口见
     *
     * @param value
     * @param context
     * @throws Exception
     * @see org.chenzc.communi.callback.RedisPipelineCallBack
     */
    @Override
    public void invoke(TrackEventEntity value, Context context) throws Exception {
        try {
            LettuceRedisUtils.pipeline(redisAsyncCommands -> {
                List<RedisFuture<?>> redisFutures = new ArrayList<>();

                /**
                 * 1.构建userId维度的链路信息 数据结构list:{key,list}
                 * key:userId,listValue:[{timestamp,state,businessId},{timestamp,state,businessId}]
                 */
                SimpleTrackEventEntity trackUserEventInfo = SimpleTrackEventEntity.builder()
                        .businessId(value.getBusinessId())
                        .state(value.getState())
                        .timestamp(value.getTimestamp())
                        .build();

//            设置过期信息 lpush  & expire
                for (String receiver : value.getReceivers()) {
                    redisFutures.add(redisAsyncCommands.lpush(receiver.getBytes(), JSON.toJSONString(trackUserEventInfo).getBytes()));
                    redisFutures.add(redisAsyncCommands.expire(receiver.getBytes(), (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000));
                }


                /**
                 * 2.构建消息模板维度的链路信息 数据结构hash:{key,hash}
                 * key:businessId,hashValue:{state,stateCount}
                 */
                redisFutures.add(redisAsyncCommands.hincrby(String.valueOf(value.getBusinessId()).getBytes()
                        , String.valueOf(value.getState()), value.getReceivers().size()));

                redisFutures.add(redisAsyncCommands.expire(String.valueOf(value.getBusinessId()).getBytes()
                        , ((DateUtil.offsetDay(new Date(), MONTH).getTime()) / 1000) - DateUtil.currentSeconds()));

                return redisFutures;
            });
        }catch (Exception e){
            log.error("AustinSink#invoke error: {}", Throwables.getStackTraceAsString(e));
        }
    }


}

