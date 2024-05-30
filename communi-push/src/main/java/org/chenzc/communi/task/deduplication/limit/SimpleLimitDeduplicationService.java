package org.chenzc.communi.task.deduplication.limit;

import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.constant.PushConstant;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.task.deduplication.service.AbstractDeduplicationService;
import org.chenzc.communi.task.deduplication.service.DeduplicationService;
import org.chenzc.communi.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


//TODO 判断消息来源渠道
/**
 * 普通计数去重 限制单用户每天能收到信息的条数
 * 由 pipelin & mget 实现
 *
 * @author chenz
 * @date 2024/05/29
 */
@Service("SimpleLimitDeduplicationService")
public class SimpleLimitDeduplicationService implements LimitDeduplicationService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    @Qualifier("FrequencyDeduplicationService")
//    标记好注入的bean是 按频次去重的bean
    private DeduplicationService deduplicationService;

    /**
     * 本程序将去重的记录存放在了Redis中 此方法本质山就是从redis中拿出对应的数据 判断他是否符合要求
     *
     * @param service 表示当前的去重业务 做唯一标识
     * @param entity  去重实体
     * @return {@link Set }<{@link String }>
     */
    @Override
    public Set<String> doLimit(AbstractDeduplicationService service, DeduplicationConfigEntity entity) {
//        整个业务流程就是取 判断 写
        TaskInfo taskInfo = entity.getTaskInfo();

//        构造key 从redis中拿出key
//        key为组装key value为用户发送次数
        List<String> deduplicationKey = deduplicationService.createDeduplicationKey(service, taskInfo);
        Map<String, String> userFrequencyValue = redisUtils.mGet(deduplicationKey);

//        获取所有的接收者的信息 并且创建一个应过滤用户的Set（存储对应用户的hexKey） 上限则为receiver总人数
        Set<String> receivers = taskInfo.getReceiver();
        Set<String> filterReceivers = new HashSet<>(receivers.size());
        Set<String> unFilterReceiversHex = new HashSet<>(receivers.size());
//        在下方的使用中 filterReceivers 存的值为接收者明文
//        unFilterReceiversHex存的值为接收者组装后的key

        for (String receiver : receivers) {
            String hexKey = deduplicationService.createSingleDeduplicationKey(taskInfo, receiver);
            String value = userFrequencyValue.get(hexKey);

//            当满足条件的时候 则代表该消息应该被去重
            if (Objects.nonNull(value) && Integer.parseInt(value) >= entity.getDeduplicationNums()) {
                filterReceivers.add(receiver);

            } else {
//                若进了else 则说明不满足去重条件
//                此前没有记录 or 记录次数 < 应去重次数
                unFilterReceiversHex.add(hexKey);
            }
//            TODO 修改为异步
            if (!unFilterReceiversHex.isEmpty()) {
                RedisAccumulateReceiver(unFilterReceiversHex, userFrequencyValue, entity.getDeduplicationTime());
            }
        }
        return filterReceivers;
    }

    /**
     * 更新不符合去重条件的接收者 的去重信息 重新写入redis中
     *
     * @param unFilterReceiversHex 不符合去重条件的接收者 set中value为组装后key
     * @param userFrequencyValue   用户去重信息
     * @param deduplicationTime    存活时间 方便redis-pipeline写入
     */
    private void RedisAccumulateReceiver(Set<String> unFilterReceiversHex, Map<String, String> userFrequencyValue, Long deduplicationTime) {
        Map<String, String> userNewFrequencyValue = new HashMap<>(unFilterReceiversHex.size());
        for (String unFilterReceiver : unFilterReceiversHex) {
            String value = userFrequencyValue.get(unFilterReceiver);
//            如果为空 则为原次数加1 如果非空 则为1
            if (Objects.isNull(value)) {
                userNewFrequencyValue.put(unFilterReceiver, CommonConstant.ONE);
            } else {
                userNewFrequencyValue.put(unFilterReceiver, String.valueOf(Integer.parseInt(value) + PushConstant.LIMIT_ACCUMULATE));
            }
        }
        redisUtils.pipelineSetEx(userNewFrequencyValue, deduplicationTime);
    }
}
