package org.chenzc.communi.utils;

import cn.hutool.extra.mail.MailAccount;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.dao.ChannelAccountDao;
import org.chenzc.communi.entity.ChannelAccount;
import org.chenzc.communi.entity.SmsAccount;
import org.chenzc.communi.enums.ChannelType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component
public class AccountUtils {

    @Resource
    private ChannelAccountDao channelAccountDao;

    /**
     * 根据id从库中获取ID 并判断是否存在
     *
     * @param sendAccountId
     * @return {@link MailAccount }
     */
    public <T> T getAccountById(Integer sendAccountId, Class<T> clazz) {
        {
            try {
                ChannelAccount channelAccount = channelAccountDao.selectById(Long.valueOf(sendAccountId));

//            获取账户 如果账户存在
                if (Objects.nonNull(channelAccount)) {

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
    public <T> T getSmsAccountByScriptName(String scriptName, Class<T> clazz) {
        try {

            QueryWrapper<ChannelAccount> qw = new QueryWrapper<>();
            qw.eq("is_delete", CommonConstant.FALSE).eq("send_channel", ChannelType.SMS.getCode());
            List<ChannelAccount> channelAccountList = channelAccountDao.selectList(qw);

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
