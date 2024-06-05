package org.chenzc.communi.service;

import org.chenzc.communi.entity.BasicResult;

/**
 * 面向外层api的定时任务相关业务类
 * @author chenz
 * @date 2024/06/05
 */
public interface CronTaskApiService {

    /**
     * 启动模板的定时任务
     *
     * @param id
     * @return {@link BasicResult }
     */
    BasicResult startCronTask(Long id);


    /**
     * 暂停模板的定时任务
     *
     * @param id
     * @return {@link BasicResult }
     */
    BasicResult stopCronTask(Long id);
}
