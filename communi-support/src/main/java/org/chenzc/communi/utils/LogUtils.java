package org.chenzc.communi.utils;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.CustomLogListener;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.entity.LogEntity;
import org.chenzc.communi.entity.TrackEventEntity;
import org.chenzc.communi.enums.TrackEventType;
import org.chenzc.communi.mq.SendMqService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 日志工具类
 *
 * @author chenz
 * @date 2024/05/31
 */
@Slf4j
@Component
public class LogUtils extends CustomLogListener {

    @Resource
    private SendMqService sendMqService;

    @Value("${communi.business.log.topic}")
    private String logTopicName;

    @Override
    public void createLog(LogDTO logDTO) throws Exception {
        log.info(JSON.toJSONString(logDTO));
    }

    /**
     * 记录当前对象信息
     * @param logEntity 日志对象
     */
    public void print(LogEntity logEntity) {
        logEntity.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(logEntity));
    }


    /**
     * 记录埋点信息
     * @param trackEventEntity 埋点运行进度
     */
    public void print(TrackEventEntity trackEventEntity){
        trackEventEntity.setTimestamp(System.currentTimeMillis());
        String jsonTraceEvent = JSON.toJSONString(trackEventEntity);
        log.info(jsonTraceEvent);

        try {
            sendMqService.send(logTopicName,jsonTraceEvent);
        } catch (Exception e) {
            log.error("LogUtils#print send mq fail! e:{},params:{}", Throwables.getStackTraceAsString(e)
                    , jsonTraceEvent);
        }
    }

    /**
     * 两者一起记录
     */
    public void print(LogEntity logEntity,TrackEventEntity trackEventEntity){
        print(logEntity);
        print(trackEventEntity);
    }
}
