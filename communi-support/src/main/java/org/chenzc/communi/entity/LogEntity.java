package org.chenzc.communi.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogEntity {

    /**
     * 日志实体 （日志记录的内容）
     */
    private Object logObject;

    /**
     * 业务类型标记
     */
    private String businessType;

    /**
     * 时间戳
     */
    private Long timestamp;

}
