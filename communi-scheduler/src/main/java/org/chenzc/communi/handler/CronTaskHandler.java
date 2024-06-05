package org.chenzc.communi.handler;


import com.dtp.core.thread.DtpExecutor;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.config.CronAsyncThreadPoolConfig;
import org.chenzc.communi.utils.ThreadPoolUtils;
import org.springframework.stereotype.Service;

/**
 * 定时任务处理类
 * （当发布定时任务的时候
 * 只是将定时任务的相关信息写进了管理中心
 * 而当定时任务中的定时到了之后 就会将任务放到这里进行处理）
 * <p>
 *  此类任务逻辑是 把到点了的定时任务交给线程池进行处理
 * @author chenz
 * @date 2024/06/04
 */
@Service
@Slf4j
public class CronTaskHandler {


    @Resource
    private TaskHandler taskHandler;

    @Resource
    private ThreadPoolUtils threadPoolUtils;


    private DtpExecutor dtpExecutor = CronAsyncThreadPoolConfig.getXxlCronExecutor();

    /**
     * 定时任务执行类
     * 将后台定时任务交给线程池进行处理
     */
    @XxlJob("communiFlowJob")
//    注解中对应的是任务处理器的名字 可见 application.properties 中的xxl.job.executor.jobHandlerName=communiFlowJob
    public void execute(){
        log.info("CronTaskHandler#execute messageTemplateId:{} cron exec!", XxlJobHelper.getJobParam());

//        注册入线程池的管理工厂当中
        threadPoolUtils.elegantShutdownRegistry(dtpExecutor);


        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        dtpExecutor.execute(() ->taskHandler.handle(messageTemplateId));
    }

}
