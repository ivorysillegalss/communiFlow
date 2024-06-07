package org.chenzc.communi.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.dao.MessageTemplateDao;
import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.entity.MessageTemplate;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.service.MessageTemplateService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Resource
    private MessageTemplateDao messageTemplateDao;

    @Override
    public BasicResult upsertMessageTemplate(MessageTemplate messageTemplate) {
        if (Objects.nonNull(messageTemplate)){
            messageTemplateDao.insert(messageTemplate);
            return BasicResult.success();
        }
        return BasicResult.fail(RespEnums.CLIENT_BAD_VARIABLES);
    }

    @Override
    public BasicResult queryMessageTemplateById(Long id) {
        MessageTemplate messageTemplate = messageTemplateDao.selectById(id);
        if (Objects.nonNull(messageTemplate)) {
            return BasicResult.success(messageTemplate);
        }
        return BasicResult.fail(RespEnums.CLIENT_BAD_VARIABLES);
    }

    @Override
    public BasicResult deleteMessageTemplateById(Long id) {
        try {
            messageTemplateDao.deleteById(id);
        }catch (Exception e){
            return BasicResult.fail(RespEnums.CLIENT_BAD_VARIABLES);
        }
        return BasicResult.success();
    }


}
