package org.chenzc.communi.service;

import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.entity.send.SendResponse;

public interface MessageTemplateService {

    /**
     * 启动模板的定时任务
     *
     * @param id
     * @return {@link SendResponse }
     */
    BasicResult startCronTask(Long id);


    /**
     * 暂停模板的定时任务
     *
     * @param id
     * @return {@link SendResponse }
     */
    BasicResult stopCronTask(Long id);
}
