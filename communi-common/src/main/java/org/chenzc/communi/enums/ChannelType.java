package org.chenzc.communi.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.chenzc.communi.content.ContentModel;
import org.chenzc.communi.content.EmailContentModel;
import org.chenzc.communi.content.SmsContentModel;

import java.util.Arrays;
import java.util.Objects;

/**
 * 发送渠道类型枚举
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum ChannelType implements PowerfulEnums {


    /**
     * sms(短信)  -- 腾讯云、云片
     */
    SMS(30, "sms(短信)", SmsContentModel.class,"sms", null, null),
    /**
     * email(邮件) -- QQ、163邮箱
     */
    EMAIL(40, "email(邮件)", EmailContentModel.class, "email", null, null);

    /**
     * 编码值
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String message;

    /**
     * 内容模型Class
     */
    private final Class<? extends ContentModel> contentModelClass;

    /**
     * 英文标识
     */
    private final String codeEn;

    /**
     * accessToken prefix
     */
    private final String accessTokenPrefix;

    /**
     * accessToken expire
     * 单位秒
     */
    private final Long accessTokenExpire;

    /**
     * 通过code获取class
     *
     * @param code
     * @return
     */
    public static Class<? extends ContentModel> getChanelModelClassByCode(Integer code) {
        return Arrays.stream(values()).filter(channelType -> Objects.equals(code, channelType.getCode()))
                .map(ChannelType::getContentModelClass)
                .findFirst().orElse(null);
    }
}
