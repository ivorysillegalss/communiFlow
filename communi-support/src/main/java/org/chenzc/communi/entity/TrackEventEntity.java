package org.chenzc.communi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 消息推送之后的日志埋点信息追踪 实体类
 * @author chenz
 * @date 2024/05/31
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TrackEventEntity {

    /**
     * 以消息为单位的唯一id
     * 以TaskInfoUtils生成
     */
    private String bizId;

    /**
     * 以消息为单位的唯一id
     * 以TaskInfoUtils生成 too
     */
    private String messageId;

    /**
     * 接收者
     */
    private Set<String> receivers;

    /**
     * 消息埋点信息
     * 具体点位可见下方枚举类
     * @see org.chenzc.communi.enums.TrackEventType
     */
    private String state;

    /**
     * 业务ID 用于进行数据追踪
     * 根据 日期 & 消息模板 等信息拼接而成
     * 相关拼接逻辑同样可见TaskInfoUtils
     */
    private Long businessId;

    /**
     * 当前的时间戳
     */
    private Long timestamp;
}
