package org.chenzc.communi.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.annonation.Handler;
import org.chenzc.communi.constant.HandlerConstant;
import org.chenzc.communi.content.EmailContentModel;
import org.chenzc.communi.entity.ChannelAccount;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.ChannelType;
import org.chenzc.communi.handler.BaseHandler;
import org.chenzc.communi.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Handler
@Slf4j
public class EmailHandler extends BaseHandler {

    /**
     * 初始化配置渠道码
     */
    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();

//        限流配置TBD TODO
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccountConfig(taskInfo.getSendAccount());
        try {
//            判断邮件内是否有附件
            List<File> files = CollUtil.isEmpty(emailContentModel.getUrl()) ? null : FileUtils.getRemoteUrl2File(HandlerConstant.URL_DATA_PATH, emailContentModel.getUrl());
            if (CollUtil.isEmpty(files)) {
                MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(), emailContentModel.getContent(), true);
            } else {
                MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(), emailContentModel.getContent(), true, files.toArray(new File[files.size()]));
            }

        } catch (Exception e) {
            log.error("EmailHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }

    /**
     * 获取账号配置
     *
     * @param sendAccount 发送的账号
     * @return {@link MailAccount }
     */
    private MailAccount getAccountConfig(Integer sendAccount) {
        MailAccount mailAccount = getAccountById(sendAccount);
        try {
            MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
            mailSSLSocketFactory.setTrustAllHosts(Boolean.TRUE);
            mailAccount.setAuth(mailAccount.isAuth())
                    .setStarttlsEnable(mailAccount.isStarttlsEnable())
                    .setSslEnable(mailAccount.isSslEnable())
                    .setCustomProperty(HandlerConstant.EMAIL_END_POINT, mailSSLSocketFactory);
        } catch (Exception e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return mailAccount;
    }

    /**
     * 根据id从库中获取ID 并判断是否存在
     *
     * @param sendAccountId
     * @return {@link MailAccount }
     */
    private MailAccount getAccountById(Integer sendAccountId) {
        {
            try {
                Optional<ChannelAccount> optionalChannelAccount = null;
//                optionalChannelAccount = channelAccountDao.findById(Long.valueOf(sendAccountId));
//                CRUD TODO

//            获取账户 如果账户存在
                if (optionalChannelAccount.isPresent()) {
                    ChannelAccount channelAccount = optionalChannelAccount.get();

                    return JSON.parseObject(channelAccount.getAccountConfig(), MailAccount.class);

                }
            } catch (Exception e) {
                log.error("AccountUtils#getAccount fail! e:{}", Throwables.getStackTraceAsString(e));
            }
            return null;
        }

    }

}
