package org.chenzc.communi.entity.trace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 查询数据时的实体类
 * @author chenz
 * @date 2024/06/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class TraceDataEntity {

    /**
     * 查看消息Id的链路信息
     */
    private String messageId;

    /**
     * 查看用户的链路信息
     */
    private String receiver;


    /**
     * 业务Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     * 如果传入的是模板ID，则生成当天的业务ID
     */
    private String businessId;


    /**
     * 日期时间(检索短信的条件使用)
     */
    private Long dateTime;


}
