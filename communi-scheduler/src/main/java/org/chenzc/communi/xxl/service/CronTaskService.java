package org.chenzc.communi.xxl.service;

import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.xxl.entity.XxlJobGroup;
import org.chenzc.communi.xxl.entity.XxlJobInfo;
import org.springframework.beans.factory.annotation.Value;

/**
 * 定时任务service类
 *
 * @author chenz
 * @date 2024/06/03
 */
public interface CronTaskService {

    /**
     * 新增 & 更新定时任务（upsert）
     * 两个业务共用这一个接口
     * @param xxlJobInfo xxl任务信息
     * @return {@link BasicResult }
     */
    BasicResult saveCronTask(XxlJobInfo xxlJobInfo);


    /**
     * 开始执行定时任务
     *
     * @param taskId 定时任务的id
     * @return {@link BasicResult }
     */
    BasicResult startCronTask(Integer taskId);


    /**
     * 得到执行器的id
     *
     * @param appName
     * @param title
     * @return {@link BasicResult }
     */
    BasicResult getGroupId(String appName, String title);


    /**
     * 创建执行器
     * @param xxlJobGroup
     * @return {@link BasicResult }
     */
    BasicResult createGroup(XxlJobGroup xxlJobGroup);

    BasicResult stopCronTask(Integer cronTaskId);
}
