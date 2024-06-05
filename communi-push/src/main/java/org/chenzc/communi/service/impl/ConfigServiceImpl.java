package org.chenzc.communi.service.impl;

import cn.hutool.setting.dialect.Props;
import jakarta.annotation.Resource;
import org.chenzc.communi.service.ConfigService;
import org.chenzc.communi.utils.NacosUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;


/**
 * 远端or本地文件读取配置实现类
 * 目前以nacos为云端配置中心
 *
 * @author chenz
 * @date 2024/06/06
 */
@Service
public class ConfigServiceImpl implements ConfigService {


    /**
     * 当没有远端配置时 本地的默认配置以及路径
     */
    private static final String PROPERTIES_PATH = "local.properties";
    private Props props = new Props(PROPERTIES_PATH, StandardCharsets.UTF_8);

    @Value("${communi.nacos.enabled}")
    private Boolean enableNacos;

    /**
     * nacos读取配置相关封装工具类
     */
    @Resource
    private NacosUtils nacosUtils;

    /**
     * 读取配置方法
     * @param key
     * @param defaultValue
     * @return {@link String }
     */
    @Override
    public String getProperty(String key, String defaultValue) {
        if (Boolean.TRUE.equals(enableNacos)) {
            return nacosUtils.getProperty(key, defaultValue);
        }
        return props.getProperty(key, defaultValue);
    }
}
