package org.chenzc.communi.task.deduplication.limit;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import org.chenzc.communi.constant.PushConstant;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.task.deduplication.service.AbstractDeduplicationService;
import org.chenzc.communi.task.deduplication.service.DeduplicationService;
import org.chenzc.communi.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;


/**
 * 滑动窗口去重 with redis-zset  lua脚本
 * 五分钟内容若收到的内容相同 则过滤
 *
 * @author chenz
 * @date 2024/05/30
 */
//TODO 这里或许可以利用其他方法达到相同的效果？  eg 时间轮算法 xxl-job分配任务
@Service(value = "SlideWindowLimitDeduplicationService")
public class SlideWindowLimitDeduplicationService implements LimitDeduplicationService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    @Qualifier("ContentDeduplicationService")
    private DeduplicationService deduplicationService;

    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
//        配置redisLua脚本相关
        redisScript = redisUtils.initialRedisScript(Long.class, PushConstant.LIMIT_LUA_SCRIPT_PATH);
    }

    /**
     * 执行去重真实逻辑
     *
     * @param service 去重服务对象
     * @param entity  去重实体规则
     * @return {@link Set }<{@link String }>
     */
//    lua脚本处理真实去重逻辑
//    所以此方法内容无非就是对每一个接收者都调用lua脚本
    @Override
    public Set<String> doLimit(AbstractDeduplicationService service, DeduplicationConfigEntity entity) {
        TaskInfo taskInfo = entity.getTaskInfo();
        Set<String> receivers = taskInfo.getReceiver();
        Set<String> filterReceivers = new HashSet<>(receivers.size());

        long nowTime = System.currentTimeMillis();
        for (String receiver : receivers) {
            String deduplicationKey = deduplicationService.createSingleDeduplicationKey(taskInfo, receiver);
//            雪花算法生成唯一value
            String scoreValue = IdUtil.getSnowflake().nextIdStr();
//            执行的lua脚本相关配置于limit.lua文件中
            Boolean isDeduplication = redisUtils.execLimitLua(redisScript,
                    Collections.singletonList(deduplicationKey),

                    String.valueOf(entity.getDeduplicationTime() * PushConstant.TO_MILLISECONDS),
//                    默认去重规则中 deduplicationTime 的单位是秒 此处转换为ms后存入数据库当中
                    String.valueOf(nowTime),
//                    存入当前的时间 （更新时间戳窗口）
                    String.valueOf(entity.getDeduplicationNums()),
//                    规定去重需求的最大次数
                    scoreValue);

            if (isDeduplication.equals(Boolean.TRUE)){
                filterReceivers.add(receiver);
            }
        }
        return filterReceivers;
    }
}
