package org.chenzc.communi.xxl.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import jakarta.annotation.Resource;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.entity.MessageTemplate;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.xxl.constant.XxlJobConstant;
import org.chenzc.communi.xxl.entity.XxlJobGroup;
import org.chenzc.communi.xxl.entity.XxlJobInfo;
import org.chenzc.communi.xxl.enums.*;
import org.chenzc.communi.xxl.service.CronTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * xxl工具的二次封装类
 *
 * @author chenz
 * @date 2024/06/03
 */
@Component
public class XxlJobsUtils {
    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.jobHandlerName}")
    private String jobHandlerName;

    @Resource
    private CronTaskService cronTaskService;

    /**
     * 组装 XxlJobInfo信息
     *
     * @param messageTemplate
     * @return {@link XxlJobInfo }
     */
    public XxlJobInfo buildXxlJobInfo(MessageTemplate messageTemplate) {
        String scheduleConf = messageTemplate.getExpectPushTime();
//        判断有无指定cron表达式 若无指定 则默认赋值为延迟5s
        if (scheduleConf.equals(String.valueOf(CommonConstant.FALSE))) {
            scheduleConf = DateUtil.format(DateUtil.offsetSecond(new Date(), XxlJobConstant.DELAY_TIME), CommonConstant.CRON_FORMAT);
        }

        XxlJobInfo xxlJobInfo = XxlJobInfo.builder()
                .jobGroup(queryJobGroupId()).jobDesc(messageTemplate.getName())
//                获取其执行器组的id已经描述
                .author(messageTemplate.getCreator())
//                指定负责人
                .scheduleConf(scheduleConf)
//                指定定时任务对应的延迟时间 此处使用cron表达式
                .scheduleType(ScheduleTypeEnum.CRON.name())
//                调度的过期策略
                .misfireStrategy(MisfireStrategyEnum.DO_NOTHING.name())
//                执行器路由策略为 一致性哈希
                .executorRouteStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
//                执行任务名称
                .executorHandler(XxlJobConstant.JOB_HANDLER_NAME)
//                执行任务参数
                .executorParam(String.valueOf(messageTemplate.getId()))
//                阻塞处理策略 ： 当一个任务的执行时间  > 执行周期
                .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
//                超时
                .executorTimeout(XxlJobConstant.TIME_OUT)
//                最大重试次数
                .executorFailRetryCount(XxlJobConstant.RETRY_COUNT)
                .glueType(GlueTypeEnum.BEAN.name())
                .triggerStatus(CommonConstant.FALSE)
                .glueRemark(CharSequenceUtil.EMPTY)
                .glueSource(CharSequenceUtil.EMPTY)
                .alarmEmail(CharSequenceUtil.EMPTY)
                .childJobId(CharSequenceUtil.EMPTY).build();

        if (Objects.nonNull(messageTemplate.getCronTaskId())) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }
        return xxlJobInfo;
    }

    /**
     *  根据配置文件的内容获取配置的jobGroupId
     *  没有则创建
     *
     * @return {@link Integer }
     */
    private Integer queryJobGroupId() {
        BasicResult<Integer> basicResult = cronTaskService.getGroupId(appName, jobHandlerName);

        //        判断是否有cookie中对应的执行器组
        if (Objects.isNull(basicResult.getData())) {
            XxlJobGroup  xxlJobGroup = XxlJobGroup.builder().appname(appName)
                    .title(jobHandlerName)
                    .addressType(CommonConstant.FALSE)
                    .build();

//            创建对应的组
            if (RespEnums.SUCCESS.getCode().equals(cronTaskService.createGroup(xxlJobGroup).getStatus())){

//                get获取id并返回
                return (Integer) cronTaskService.getGroupId(appName,jobHandlerName).getData();
            }
        }

        return basicResult.getData();
    }
}
