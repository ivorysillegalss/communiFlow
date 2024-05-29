package org.chenzc.communi.task.deduplication.builder;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.chenzc.communi.constant.PushConstant;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.DeduplicationFactory;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.task.deduplication.service.AbstractDeduplicationService;
import org.chenzc.communi.utils.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractDeduplicationBuilder implements DeduplicationBuilder {

    //    抽象类中定义此变量 在子实现类中进行赋值
    protected String deduplicationType;

    @Resource
    private DeduplicationFactory deduplicationFactory;

    /**
     * 生命周期方法 将对应的子类实现类对象加入工厂当中
     */
    @PostConstruct
    public void init() {
//        此方法会被此抽象类的实现类所调用
        deduplicationFactory.putBuilder(deduplicationType, this);
    }


    /**
     * 在程序的架构实现中规定每一个去重方法的相关配置都保存在配置文件中
     * 而这个配置文件可以是远端的配置中心
     * 也可以是本地的配置文件
     *
     * @param deduplicationType   去重的类型
     * @param deduplicationConfig 去重配置
     * @param taskInfo            任务信息
     * @return {@link DeduplicationConfigEntity }
     */
    protected DeduplicationConfigEntity parseEntityFromConfig(String deduplicationType, String deduplicationConfig, TaskInfo taskInfo) {
        JSONObject jsonObject = JSON.parseObject(deduplicationConfig);

//        先通过类型转换判断是否一个合法的结构体
        if (Objects.isNull(jsonObject)) return null;

        DeduplicationConfigEntity entity = JSON.parseObject(jsonObject.getString(StringUtils.append(PushConstant.DEDUPLICATION_CONFIG_PREFIX, deduplicationType)), DeduplicationConfigEntity.class);
        if (Objects.isNull(entity)) return null;

        return entity.setTaskInfo(taskInfo);
    }

    /**
     * 利用md5创建去重key
     *
     * @param service  去重服务 用于作唯一标识
     * @param taskInfo 任务信息
     * @return {@link List }<{@link String }>
     */
    @Override
    public String createSingleDeduplicationKey(AbstractDeduplicationService service, TaskInfo taskInfo,String receiver){
        String hexStr = DigestUtil.md5Hex(StringUtils.append(String.valueOf(taskInfo.getMessageTemplateId()),
                receiver, JSON.toJSONString(taskInfo.getContentModel())));
        return StringUtils.append(PushConstant.LIMIT_TAG_PREFIX,hexStr);
    }


    /**
     * 创建一堆去重key
     */
    @Override
    public List<String> createDeduplicationKey(AbstractDeduplicationService service, TaskInfo taskInfo) {
        Set<String> receivers = taskInfo.getReceiver();
        List<String> result = new ArrayList<>(receivers.size());
        for (String receiver : receivers) {
            String hexStr = createSingleDeduplicationKey(service, taskInfo, receiver);
            result.add(StringUtils.append(hexStr, PushConstant.LIMIT_TAG_PREFIX));
        }
        return result;
    }
}
