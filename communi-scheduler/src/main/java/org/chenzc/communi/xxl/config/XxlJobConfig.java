package org.chenzc.communi.xxl.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job相关配置类
 * @author chenz
 * @date 2024/06/04
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "communi.xxl.job.enabled",havingValue = "true")
public class XxlJobConfig {
    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.ip}")
    private String ip;
//    可根据需要进行进一步配置
    //    @Value("${xxl.job.executor.address}")
//    private String address;
//    @Value("${xxl.job.executor.port}")
//    private int port;
//    @Value("${xxl.job.accessToken}")
//    private String accessToken;
//    @Value("${xxl.job.executor.logpath}")
//    private String logPath;
//    @Value("${xxl.job.executor.logretentiondays}")
//    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor(){
        log.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxl-joooooooooooooooooooob init");
//        创建 XxlJobSpringExecutor执行器
            XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
            xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
            xxlJobSpringExecutor.setAppname(appName);
            xxlJobSpringExecutor.setAccessToken(accessToken);
            xxlJobSpringExecutor.setPort(port);
            xxlJobSpringExecutor.setIp(ip);
//            可根据需要进行进一步配置

            return xxlJobSpringExecutor;
    }

}
