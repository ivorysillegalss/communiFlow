package org.chenzc.communi.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChannelAccount {

    private Long id;

    private String name;

    /**
     * 发送渠道
     * @see org.chenzc.communi.enums.ChannelType
     */
    private String sendChannel;

    /**
     * 账号配置
     */
    private String accountConfig;

    /**
     *逻辑删除
     */
    private Integer isDelete;
}
