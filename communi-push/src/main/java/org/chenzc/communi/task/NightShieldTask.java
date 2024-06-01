package org.chenzc.communi.task;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.chenzc.communi.constant.TaskConstant;
import org.chenzc.communi.entity.*;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.enums.ShieldType;
import org.chenzc.communi.enums.TrackEventType;
import org.chenzc.communi.executor.TaskNodeModel;
import org.chenzc.communi.utils.LogUtils;
import org.chenzc.communi.utils.RedisUtils;
import org.chenzc.communi.utils.TaskInfoUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 消息夜间屏蔽任务节点
 * <p>
 * 若在白天 则正常下发
 * <p>
 * 若在晚上
 * 根据任务信息的配置 判断当前信息是否发送
 *
 * @author chenz
 * @date 2024/05/31
 */

@Service
public class NightShieldTask implements TaskNodeModel<TaskInfo> {

    @Resource
    private LogUtils logUtils;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 整个任务节点的逻辑：
     * 根据消息模板信息判断 是否需要进行夜间屏蔽或否的节点的信息
     * 分为以上三种情况进行分类处理
     *
     * @param taskContext
     */
    @Override
    public void execute(TaskContext<TaskInfo> taskContext) {
        TaskInfo taskInfo = taskContext.getBusinessContextData();
        String shieldType = taskInfo.getShieldType();

//        1. 夜间无需进行屏蔽
        if (shieldType.equals(ShieldType.NOT_NIGHT_SHIELD.getCode())) return;

//        2. 夜间需要进行屏蔽

//        获取当前的时间 判断是否夜间
        if (LocalDateTime.now().getHour() < TaskConstant.NIGHT_TIME){

//            2.1 夜间屏蔽
            if (shieldType.equals(ShieldType.NIGHT_SHIELD.getCode())) {
                logUtils.print(
                        LogEntity.builder().build()
                        ,
                        TrackEventEntity.builder()
                                .bizId(taskInfo.getBizId())
                                .timestamp(System.currentTimeMillis())
                                .receivers(taskInfo.getReceiver())
                                .messageId(TaskInfoUtils.generateMessageId())
                                .businessId(TaskInfoUtils.generateBusinessId(taskInfo.getMessageTemplateId(), taskInfo.getTemplateType()))
                                .state(TrackEventType.NIGHT_SHIELD.getCode())
                                .build()
                );

                taskContext.setException(Boolean.TRUE)
                        .setResponse(TaskContextResponse.<TaskInfo>builder()
                                .code(RespEnums.MESSAGE_NIGHT_SHIELD.getCode())
                                .build());
            }


//            2.2 消息夜间屏蔽 次日发送
//        根据程序架构 后面将会接入定时框架 完成定时任务功能 此处使用夜间屏蔽
//        并将消息放入消息队列中存储 第二天直接发布定时任务扫表 统一处理 也可以分配更好的解决方法 TODO
            if (shieldType.equals(ShieldType.NIGHT_SHIELD_NEXT_DAY_SEND.getCode())) {

                redisUtils.lPush(TaskConstant.NIGHT_SHIELD_NEXT_DAY_SEND_KEY,
                        JSON.toJSONString(taskInfo, SerializerFeature.WriteClassName),
                        TaskConstant.SECONDS_OF_A_DAY);

                logUtils.print(
                        LogEntity.builder().build()
                        ,
                        TrackEventEntity.builder()
                                .bizId(taskInfo.getBizId())
                                .receivers(taskInfo.getReceiver())
                                .state(TrackEventType.NIGHT_SHIELD_NEXT_SEND.getCode())
                                .messageId(taskInfo.getMessageId())
                                .businessId(taskInfo.getBusinessId())
                                .timestamp(System.currentTimeMillis())
                                .build()
                );

                taskContext.setException(Boolean.TRUE)
                        .setResponse(TaskContextResponse.<TaskInfo>builder()
                                .code(RespEnums.MESSAGE_NIGHT_SHIELD_NEXT_DAY_SEND.getCode())
                                .build());

            }
        }

    }

}
