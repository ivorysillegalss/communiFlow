package org.chenzc.communi.entity.send;


import lombok.Builder;
import lombok.Data;

/**
 * 请求响应后的内容
 * @author chenz
 * @date 2024/05/22
 */
@Builder
@Data
public class SendResponse {
    /**
     * 消息响应码 是否成功？   结合common中的RespEnums
     */
    private String code;

    /**
     * 响应体中若包含数据 则需返回的数据
     */
//    private T data;
}
