package org.chenzc.communi.controller;

import cn.hutool.core.text.CharSequenceUtil;
import jakarta.annotation.Resource;
import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.entity.MessageTemplate;
import org.chenzc.communi.entity.trace.MessageInfo;
import org.chenzc.communi.entity.trace.TraceDataEntity;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.service.DataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


/**
 * 获取数据的接口  （全链路追踪）
 * 三维度查询 用户 & 消息模板 & BusinessId or MessageTemplateId
 *
 * @author chenz
 * @date 2024/06/13
 */
@RestController
@RequestMapping("/trace")
public class DataController {

    @Resource
    private DataService dataService;

    @PostMapping("/message")
    public BasicResult getMessageData(@RequestBody TraceDataEntity traceDataEntity) {
        if (Objects.isNull(traceDataEntity) || CharSequenceUtil.isBlank(traceDataEntity.getMessageId())) {
            return BasicResult.fail(RespEnums.CLIENT_BAD_VARIABLES);
        }
        MessageInfo messageInfoById = dataService.getMessageInfoById(traceDataEntity.getMessageId());
        if (Objects.isNull(messageInfoById)) return BasicResult.fail(RespEnums.CLIENT_BAD_VARIABLES);
        return BasicResult.success(messageInfoById);
    }

    @PostMapping("/user")
    public BasicResult getUserData(@RequestBody TraceDataEntity traceDataEntity) {
        if (Objects.isNull(traceDataEntity) || CharSequenceUtil.isBlank(traceDataEntity.getMessageId())) {
            return BasicResult.fail(RespEnums.CLIENT_BAD_VARIABLES);
        }
        MessageInfo traceUserInfo = dataService.getTraceUserInfo(traceDataEntity.getReceiver());
        if (Objects.isNull(traceUserInfo)) return BasicResult.fail(RespEnums.CLIENT_BAD_VARIABLES);
        return BasicResult.success(traceUserInfo);
    }

    @PostMapping("/id")
    public BasicResult getMessageTemplateData(@RequestBody TraceDataEntity traceDataEntity) {
        if (Objects.isNull(traceDataEntity) || CharSequenceUtil.isBlank(traceDataEntity.getMessageId())) {
            return BasicResult.fail(RespEnums.CLIENT_BAD_VARIABLES);
        }
        MessageTemplate traceMessageTemplateInfo = dataService.getTraceMessageTemplateInfo(traceDataEntity.getBusinessId());
        if (Objects.isNull(traceMessageTemplateInfo)) return BasicResult.fail(RespEnums.CLIENT_BAD_VARIABLES);
        return BasicResult.success(traceMessageTemplateInfo);
    }
}