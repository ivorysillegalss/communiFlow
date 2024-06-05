package org.chenzc.communi.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import org.chenzc.communi.dao.MessageTemplateDao;
import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.entity.MessageTemplate;
import org.chenzc.communi.enums.MessageStatus;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.service.CronTaskApiService;
import org.chenzc.communi.xxl.entity.XxlJobInfo;
import org.chenzc.communi.xxl.utils.XxlJobsUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CronTaskApiServiceImpl implements CronTaskApiService {

    @Resource
    private XxlJobsUtils xxlJobsUtils;

    @Resource
    private MessageTemplateDao messageTemplateDao;

    @Resource
    private org.chenzc.communi.xxl.service.CronTaskService cronTaskService;

    @Override
    public BasicResult startCronTask(Long id) {
//        查询此定时任务对应的 消息模板
        MessageTemplate messageTemplate = messageTemplateDao.selectById(id);
        if (Objects.isNull(messageTemplate)) {
            return BasicResult.fail();
        }

        XxlJobInfo xxlJobInfo = xxlJobsUtils.buildXxlJobInfo(messageTemplate);
        Integer cronTaskId = messageTemplate.getCronTaskId();
        BasicResult basicResult = cronTaskService.saveCronTask(xxlJobInfo);

//        这里的判断是 判断是否为新增的消息模板 & 判断新建消息模板的请求有没有成功
//        如果成功就取这一个id为taskId赋值
        if (Objects.isNull(cronTaskId) && RespEnums.SUCCESS.getCode().equals(basicResult.getStatus()) && Objects.nonNull(basicResult.getData())) {
            cronTaskId = Integer.valueOf(String.valueOf(basicResult.getData()));
        }

//        如果在xxl模板信息这里没有问题的话 就直接启动 执行定时任务
        if (Objects.nonNull(cronTaskId)) {
            cronTaskService.startCronTask(cronTaskId);

            MessageTemplate cloneMessageTemplate = ObjectUtil.clone(messageTemplate).
                    setMsgStatus(MessageStatus.RUN.getCode())
                    .setCronTaskId(cronTaskId)
                    .setUpdated(Math.toIntExact(DateUtil.currentSeconds()));

            messageTemplateDao.insert(cloneMessageTemplate);
            return BasicResult.success();
        }

        return BasicResult.fail(RespEnums.CRON_TASK_SERVICE_ERROR);
    }

    @Override
    public BasicResult stopCronTask(Long id) {
//    修改模板状态后 再暂停定时任务
        MessageTemplate messageTemplate = messageTemplateDao.selectById(id);
        if (Objects.isNull(messageTemplate)){
            return BasicResult.fail();
        }

        MessageTemplate cloneMessageTemplate = ObjectUtil.clone(messageTemplate)
                .setMsgStatus(MessageStatus.STOP.getMessage())
                .setCronTaskId(Math.toIntExact(DateUtil.currentSeconds()));
        messageTemplateDao.insert(cloneMessageTemplate);

        return cronTaskService.stopCronTask(cloneMessageTemplate.getCronTaskId());
    }
}
