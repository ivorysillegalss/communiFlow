package org.chenzc.communi.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.entity.TaskContext;
import org.chenzc.communi.entity.TaskContextResponse;
import org.chenzc.communi.entity.TaskInfo;
import org.chenzc.communi.enums.IdType;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.executor.TaskNodeModel;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AfterCheckTask implements TaskNodeModel<SendContextData> {

    /**
     * 邮件和手机号正则
     */

    public static final String PHONE_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";
    public static final String EMAIL_REGEX_EXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static Map<Integer, String> CHANNEL_REGEX_EXP;


    /**
     * 初始化正则表达式
     */
    @PostConstruct
    public void init() {
        CHANNEL_REGEX_EXP = new HashMap<>();
        CHANNEL_REGEX_EXP.put(IdType.PHONE.getCode(), PHONE_REGEX_EXP);
        CHANNEL_REGEX_EXP.put(IdType.EMAIL.getCode(), EMAIL_REGEX_EXP);
    }

    @Override
    public void execute(TaskContext<SendContextData> taskContext) {
        List<TaskInfo> taskInfos = taskContext.getBusinessContextData().getTaskInfos();
        filterIllegalReceiver(taskInfos);
        if (CollUtil.isEmpty(taskInfos)) {
            taskContext.setException(Boolean.TRUE).setResponse(TaskContextResponse.<SendContextData>builder()
                    .code(RespEnums.CLIENT_BAD_PARAMETERS.getCode()).build());
        }
    }

    /**
     * 根据指定的任务信息 判断格式是否合法
     *
     * @param taskInfos 任务类型信息
     */
    private void filterIllegalReceiver(List<TaskInfo> taskInfos) {
        Integer idType = CollUtil.getFirst(taskInfos.listIterator()).getIdType();
        String regex = CHANNEL_REGEX_EXP.get(idType);
        for (TaskInfo taskInfo : taskInfos) {
            Set<String> legalReceivers = taskInfo.getReceiver().stream().filter(num -> ReUtil.isMatch(regex, num))
                    .collect(Collectors.toSet());
            taskInfo.setReceiver(legalReceivers);
        }
    }
}
