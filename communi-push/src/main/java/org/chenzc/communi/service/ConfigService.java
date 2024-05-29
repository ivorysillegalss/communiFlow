package org.chenzc.communi.service;

/**
 * 读取配置相关的接口
 *
 * @author chenz
 * @date 2024/05/29
 */
public interface ConfigService {

    /**
     * 从本地文件or远端配置中心（TBD）中读取配置
     * @return {@link String }
     */

//    TODO 读取远端配置文件信息
    String getProperty(String key,String defaultValue);
}
