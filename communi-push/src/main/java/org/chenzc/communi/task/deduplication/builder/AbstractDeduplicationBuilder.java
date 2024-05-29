package org.chenzc.communi.task.deduplication.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.chenzc.communi.constant.PushConstant;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.DeduplicationFactory;
import org.chenzc.communi.task.deduplication.entity.DeduplicationEntity;
import org.chenzc.communi.utils.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

public abstract class AbstractDeduplicationBuilder implements DeduplicationBuilder {

    //    抽象类中定义此变量 在子实现类中进行赋值
    protected String deduplicationType;

    @Resource
    private DeduplicationFactory deduplicationFactory;

    @PostConstruct
    public void init() {
//        此方法会被此抽象类的实现类所调用
        deduplicationFactory.putBuilder(deduplicationType, this);
    }


    //    在程序的架构实现中规定每一个去重方法的相关配置都保存在配置文件中
//    而这个配置文件可以是远端的配置中心
//    也可以是本地的配置文件
    protected DeduplicationEntity parseEntityFromConfig(String deduplicationType, String deduplicationConfig, TaskInfo taskInfo) {
        JSONObject jsonObject = JSON.parseObject(deduplicationConfig);

//        先通过类型转换判断是否一个合法的结构体
        if (Objects.isNull(jsonObject)) return null;

        DeduplicationEntity entity = JSON.parseObject(jsonObject.getString(StringUtils.append(PushConstant.DEDUPLICATION_CONFIG_PREFIX, deduplicationType)), DeduplicationEntity.class);
        if (Objects.isNull(entity)) return null;

        return entity.setTaskInfo(taskInfo);
    }
}
