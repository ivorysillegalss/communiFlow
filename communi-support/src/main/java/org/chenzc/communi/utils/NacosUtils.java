package org.chenzc.communi.utils;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.StringReader;
import java.util.Properties;

/**
 * nacos读取配置相关工具类封装
 *
 * @author chenz
 * @date 2024/06/06
 */
@Component
@Slf4j
public class NacosUtils {

    private final int DEFAULT_TIME_OUT = 5000;

    private final Properties properties = new Properties();

    @NacosInjected
    private ConfigService configService;

    @Value("${nacos.group}")
    private String nacosGroup;

    @Value("${nacos.data-id}")
    private String nacosDataId;

    /**
     * 读取配置 or 返回默认值
     * 目前的设计是 每一次读取的时候 都会访问一次远端配置文件
     * 但是这样的设计不一定合理 频繁的读取文件对带宽要求很高
     * （每一次的调用配置都是一次rpc调用）
     *
     * @param key
     * @param defaultValue
     * @return {@link String }
     */
//TODO  所以 也许可以自定义一个类似lua脚本的算法来平衡？
// 又或者是 可以将是否每一次读取 都调用远端配置文件的这一次操作 策略化  （可以自动配置 是否需要读取热更新的配置）
//    但是本质上 或许只有需要频繁热更新的配置数据才会放在nacos中？ eg动态线程池参数
    public String getProperty(String key, String defaultValue) {
        try {
            String contextProperty = getContext();
            if (StringUtils.hasText(contextProperty)) {
                properties.load(new StringReader(contextProperty));
            }
        } catch (Exception e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        String property = properties.getProperty(key);
        return CharSequenceUtil.isBlank(property) ? defaultValue : property;
    }


    /**
     * 读取配置对应的上下文
     * 获取到对应的group中 对应的dataid 中
     * 对应的配置文件的所有具体内容
     *
     * @return {@link String }
     */
    private String getContext() {
        String context = null;
        try {
            context = configService.getConfig(nacosDataId, nacosGroup, DEFAULT_TIME_OUT);
        } catch (Exception e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        return context;
    }
}
