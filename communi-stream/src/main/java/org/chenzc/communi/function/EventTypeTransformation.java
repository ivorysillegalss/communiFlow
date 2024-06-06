package org.chenzc.communi.function;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.chenzc.communi.entity.TrackEventEntity;
import org.chenzc.communi.enums.TrackEventType;

/**
 * 处理链中的一环
 *
 * @author chenz
 * @date 2024/06/06
 */
public class EventTypeTransformation implements FlatMapFunction<String, TrackEventEntity> {
    /**
     * 实际数据转换 or 处理的逻辑
     * 以函数为单位存在 一个函数就是一个类
     *
     * @param value
     * @param collector
     * @throws Exception
     */
    @Override
    public void flatMap(String value, Collector<TrackEventEntity> collector) throws Exception {
        TrackEventEntity trackEventEntity = JSON.parseObject(value, TrackEventEntity.class);
        collector.collect(trackEventEntity);
    }
}
