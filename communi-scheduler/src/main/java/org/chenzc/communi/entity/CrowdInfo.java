package org.chenzc.communi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


/**
 *  每一行csv的记录格式
 * @author chenz
 * @date 2024/06/04
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrowdInfo implements Serializable {

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 接收者id
     */
    private String receiver;

    /**
     * 参数信息
     */
    private Map<String, String> params;
}
