package org.chenzc.communi.task.deduplication.service;

import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.task.deduplication.entity.DeduplicationConfigEntity;
import org.chenzc.communi.task.deduplication.limit.LimitDeduplicationService;

import java.util.List;

/**
 * 执行的去重服务
 *
 * @author chenz
 * @date 2024/05/28
 */
public interface DeduplicationService {

    /**
     * 去重业务方法
     * @param entity 去重规则实体
     */
    void deduplication(DeduplicationConfigEntity entity);

    /**
     *     创建一堆key 在抽象类中实现 通过批量调用各子类实现类的造key方法实现
     * @param service 标记指定的服务
     * @param taskInfo 任务信息
     * @return {@link List }<{@link String }>
     */
    List<String> createDeduplicationKey(AbstractDeduplicationService service,TaskInfo taskInfo);

    /**
     * 创建单个去重key 在各自的实现类中
     * @param taskInfo 任务信息
     * @param receiver 指定接收者
     * @return {@link String }
     */
//    目前方法的实现是在各类当中的 可以改为写在抽象类当中 TODO
//    在抽象类中定义一个 DeduplicationBuilder 编写构造函数注入当前builder实例
//    调用对应实例的createSingleDeduplicationKey方法 本质上是一样的
//    但是随着去重类型的不同 写道各服务类中可能更便于拓展

    String createSingleDeduplicationKey(TaskInfo taskInfo, String receiver);
}
