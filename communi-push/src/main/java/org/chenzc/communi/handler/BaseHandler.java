package org.chenzc.communi.handler;

import org.chenzc.communi.entity.LogEntity;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.entity.TrackEventEntity;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.utils.LogUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 所有第三方消息推送类的父类 包含其中一些共用的方法以及初始化
 *
 * @author chenz
 * @date 2024/05/27
 */
public abstract class BaseHandler implements Handler {

    @Resource
    private HandlerFactory handlerFactory;

    @Resource
    private LogUtils logUtils;

    /**
     * 表示对应渠道的码 在子类初始化对应的
     */
    protected String channelCode;

    /**
     * 处理器同一对象
     *
     * @param taskInfo
     *///    这一个类可对需要进行任务的信息 进行批处理 例如限流等 TODO
//    但是我觉得限流可以单开一个新的责任链节点去搞 ? TODO
    @Override
    public void doHandler(TaskInfo taskInfo) {
        TrackEventEntity trackEvent = TrackEventEntity.builder()
                .businessId(taskInfo.getBusinessId())
                .messageId(taskInfo.getMessageId())
                .bizId(taskInfo.getBizId())
                .build();

        if (handler(taskInfo)) {
            logUtils.print(LogEntity.builder().build(), trackEvent.setState(RespEnums.MESSAGE_HANDLE_SUCCESS.getCode()));
        } else {
            logUtils.print(LogEntity.builder().build(), trackEvent.setState(RespEnums.MESSAGE_HANDLE_ERROR.getCode()));
        }
    }

    @PostConstruct
    public void init() {
        handlerFactory.putHandler(channelCode, this);
    }

    /**
     * 调用第三方api服务
     *
     * @param taskInfo
     * @return boolean
     */
    public abstract boolean handler(TaskInfo taskInfo);
}
