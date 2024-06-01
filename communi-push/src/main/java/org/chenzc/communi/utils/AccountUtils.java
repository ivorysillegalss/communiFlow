package org.chenzc.communi.utils;

import cn.hutool.extra.mail.MailAccount;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.entity.ChannelAccount;
import org.chenzc.communi.entity.SmsAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AccountUtils {
    /**
     * 根据id从库中获取ID 并判断是否存在
     *
     * @param sendAccountId
     * @return {@link MailAccount }
     */
    public static <T> T getAccountById(Integer sendAccountId, Class<T> clazz) {
        {
            try {
                Optional<ChannelAccount> optionalChannelAccount = null;
//                optionalChannelAccount = channelAccountDao.findById(Long.valueOf(sendAccountId));
//                CRUD TODO

//            获取账户 如果账户存在
                if (optionalChannelAccount.isPresent()) {
                    ChannelAccount channelAccount = optionalChannelAccount.get();

                    return JSON.parseObject(channelAccount.getAccountConfig(), clazz);

                }
            } catch (Exception e) {
                log.error("AccountUtils#getAccount fail! e:{}", Throwables.getStackTraceAsString(e));
            }
            return null;
        }

    }


    /**
     * 通过脚本名 匹配到对应的短信账号
     *
     * @param scriptName 脚本名
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getSmsAccountByScriptName(String scriptName, Class<T> clazz) {
        try {
            List<ChannelAccount> channelAccountList = new ArrayList<>();

//            CRUD TODO
//            channelAccountList = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.SMS.getCode());

            for (ChannelAccount channelAccount : channelAccountList) {
                try {
                    SmsAccount smsAccount = JSON.parseObject(channelAccount.getAccountConfig(), SmsAccount.class);
                    if (smsAccount.getScriptName().equals(scriptName)) {
                        return JSON.parseObject(channelAccount.getAccountConfig(), clazz);
                    }
                } catch (Exception e) {
                    log.error("AccountUtils#getSmsAccount parse fail! e:{},account:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(channelAccount));
                }
            }
        } catch (Exception e) {
            log.error("AccountUtils#getSmsAccount fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        log.error("AccountUtils#getSmsAccount not found!:{}", scriptName);
        return null;
    }

}
