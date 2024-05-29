package org.chenzc.communi.task.deduplication.entity;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.TrackEventType;

/**
 * 执行去重时所需参数 （结合具体的去重策略记录）
 *
 * @author chenz
 * @date 2024/05/28
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class DeduplicationConfigEntity {

    /**
     * 任务信息
     */
    private TaskInfo taskInfo;

    /**
     * 去重时间 （N分钟内多次放松信息则去重）
     */
    @JSONField(name = "time")
    private Long deduplicationTime;

    /**
     * 去重次数 一天之内发送了N次则去重
     */
    @JSONField(name = "num")
    private Integer deduplicationNums;

    /**
     * 标识状态 （数据链路追踪）
     */
    private TrackEventType eventType;

    /**
     * 表示在构建去重参数的时候是否发生错误
     */
    private Boolean exception;
}
