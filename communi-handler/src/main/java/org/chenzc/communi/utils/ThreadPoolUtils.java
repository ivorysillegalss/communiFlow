package org.chenzc.communi.utils;


import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import org.chenzc.communi.config.ThreadPoolExecutorShutdownDefinition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ThreadPoolUtils {
    private static final String SOURCE_NAME = "chenzc";

    @Resource
    private ThreadPoolExecutorShutdownDefinition threadPoolExecutorShutdownDefinition;

    /**
     * 优雅关闭 ——> 利用applicationListener监听程序关闭  在程序关闭的时候自动注册优雅关闭
     * 首先将对应的线程池注册入动态配置
     * 然后配合工厂类中方法实现
     */
    public void elegantShutdownRegistry(DtpExecutor dtpExecutor){
        DtpRegistry.register(dtpExecutor,SOURCE_NAME);
        threadPoolExecutorShutdownDefinition.registryExecutor(dtpExecutor);
    }
}
