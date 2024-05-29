package org.chenzc.communi.service.impl;

import cn.hutool.setting.dialect.Props;
import org.chenzc.communi.service.ConfigService;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Override
    public String getProperty(String key, String defaultValue) {
        Props props = new Props();
        return props.getProperty(key, defaultValue);
    }
}
