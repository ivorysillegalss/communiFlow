package org.chenzc.communi.service;

import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.entity.MessageTemplate;
import org.springframework.transaction.annotation.Transactional;

public interface MessageTemplateService {

    @Transactional
    BasicResult upsertMessageTemplate(MessageTemplate messageTemplate);

    BasicResult queryMessageTemplateById(Long id);

    @Transactional
    BasicResult deleteMessageTemplateById(Long id);
}
