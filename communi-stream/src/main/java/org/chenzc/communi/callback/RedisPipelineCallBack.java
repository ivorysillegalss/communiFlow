package org.chenzc.communi.callback;


import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.List;

/**
 * redis pipeLine接口定义
 * @author chenz
 * @date 2024/06/06
 */
public interface RedisPipelineCallBack {

    public List<RedisFuture<?>> invoke(RedisAsyncCommands redisAsyncCommands);
}
