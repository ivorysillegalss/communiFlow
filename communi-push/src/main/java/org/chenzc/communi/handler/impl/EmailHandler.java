package org.chenzc.communi.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.annonation.Handler;
import org.chenzc.communi.constant.FlowControlConstant;
import org.chenzc.communi.constant.HandlerConstant;
import org.chenzc.communi.content.EmailContentModel;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.ChannelType;
import org.chenzc.communi.enums.RateLimitStrategy;
import org.chenzc.communi.flowcontrol.FlowControlParam;
import org.chenzc.communi.handler.BaseHandler;
import org.chenzc.communi.utils.AccountUtils;
import org.chenzc.communi.utils.FileUtils;


import java.io.File;
import java.util.List;

@Handler
@Slf4j
public class EmailHandler extends BaseHandler {

    @Resource
    private AccountUtils accountUtils;

    /**
     * 初始化配置渠道码
     */
    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();

        double rateInitValue = FlowControlConstant.DEFAULT_PERMITS.doubleValue();
        flowControlParam = FlowControlParam.builder()
                .rateInitValue(rateInitValue)
                .rateLimiter(RateLimiter.create(rateInitValue))
                .rateLimitStrategy(RateLimitStrategy.REQUEST_RATE_LIMIT)
                .build();
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
        MailAccount mailAccount = accountUtils.getAccountById(sendAccount, MailAccount.class);
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

}
