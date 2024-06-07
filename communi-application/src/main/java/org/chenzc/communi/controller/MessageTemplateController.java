package org.chenzc.communi.controller;

import jakarta.annotation.Resource;
import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.entity.MessageTemplate;
import org.chenzc.communi.entity.messageTemplate.MessageParam;
import org.chenzc.communi.entity.send.SendRequest;
import org.chenzc.communi.entity.send.SendResponse;
import org.chenzc.communi.service.MessageTemplateService;
import org.chenzc.communi.service.SendService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messageTemplate/")
public class MessageTemplateController {
    @Resource
    private SendService sendService;

    @Resource
    private MessageTemplateService messageTemplateService;

    @PostMapping("upsert")
    public BasicResult upsertMessageTemplate(@RequestBody MessageTemplate messageTemplate) {
        return messageTemplateService.upsertMessageTemplate(messageTemplate);
    }


    @GetMapping("query/{id}")
    public BasicResult queryMessageTemplateById(@PathVariable("id") Long id) {
        return messageTemplateService.queryMessageTemplateById(id);
    }


    @DeleteMapping("delete/{id}")
    public BasicResult deleteByIds(@PathVariable("id") Long id) {
        return messageTemplateService.deleteMessageTemplateById(id);
    }

    @PostMapping("test/send/{id}")
    public BasicResult testSend(@RequestBody MessageParam messageParam, @PathVariable("id") Long id) {
        SendResponse sendResponse = sendService.sendMessage(SendRequest.builder().messageTemplateId(id).messageParam(messageParam).build());
        return BasicResult.builder().data(sendResponse.getCode()).build();
    }
}
