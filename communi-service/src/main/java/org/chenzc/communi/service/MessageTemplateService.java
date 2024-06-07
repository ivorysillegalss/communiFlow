package org.chenzc.communi.service;

import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.entity.MessageTemplate;

public interface MessageTemplateService {
    BasicResult upsertMessageTemplate(MessageTemplate messageTemplate);

    BasicResult queryMessageTemplateById(Long id);

    BasicResult deleteMessageTemplateById(Long id);
}
