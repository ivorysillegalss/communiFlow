package org.chenzc.communi.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.csv.CsvRow;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.chenzc.communi.csv.CountFileRowHandler;
import org.chenzc.communi.dao.MessageTemplateDao;
import org.chenzc.communi.entity.CrowdInfo;
import org.chenzc.communi.entity.MessageTemplate;
import org.chenzc.communi.pending.AbstractLazyPending;
import org.chenzc.communi.pending.CrowdBatchTaskPending;
import org.chenzc.communi.utils.ReadFileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 定时任务处理实现类
 *
 * @author chenz
 * @date 2024/06/04
 */
@Service
@Slf4j
public class TaskHandlerImpl implements TaskHandler {

    @Resource
    private MessageTemplateDao messageTemplateDao;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void handle(Long messageTemplateId) {
        MessageTemplate messageTemplate = messageTemplateDao.selectById(messageTemplateId);
        if (Objects.isNull(messageTemplate)) {
            return;
        }
        if (CharSequenceUtil.isBlank(messageTemplate.getCronCrowdPath())) {
            log.error("TaskHandler#handle crowdPath empty! messageTemplateId:{}", messageTemplateId);
            return;
        }
//        获取文件中的行数大小
        long countCsvRow = ReadFileUtils.countCsvRow(messageTemplate.getCronCrowdPath(), new CountFileRowHandler());

//        指定应该初始化的子类
        CrowdBatchTaskPending crowdBatchTaskPending = applicationContext.getBean(CrowdBatchTaskPending.class);
        ReadFileUtils.getCsvRow(messageTemplate.getCronCrowdPath(), row -> {
//                    判断内容是否为空
            if (CollUtil.isEmpty(row.getFieldMap())
                    || CharSequenceUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                return;
            }

            Map<String, String> param = ReadFileUtils.getParamFromLine(row.getFieldMap());

            CrowdInfo crowdInfo = CrowdInfo.builder()
                    .receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                    .params(param)
                    .messageTemplateId(messageTemplateId)
                    .build();

            crowdBatchTaskPending.pending(crowdInfo);

            onComplete(row, countCsvRow, crowdBatchTaskPending, messageTemplateId);
        });
    }

    /**
     * 文件遍历结束时 暂停线程池的消费 最后回收线程池的资源
     *
     * @param csvRow
     * @param countCsvRow
     * @param crowdBatchTaskPending
     * @param messageTemplateId
     */
//         * 更改消息模板的状态 TODO
    //    引入线程池 通过线程池在规定的时间内 判断线程中资源是否发送完毕 如果发送完毕则终止线程
//    又或者是 此处的任务都是需要使用的时候获取对应的bean创建任务队列对象的
//    也许可以将bean池化 在需要用的时候在池中获取对应的队列来执行任务 TODO

//    同时这里的onComplete方法 实质上。。意义不大 只是作为一个链路追踪
    private void onComplete(CsvRow csvRow, long countCsvRow, AbstractLazyPending crowdBatchTaskPending, Long messageTemplateId) {
        if (csvRow.getOriginalLineNumber() == countCsvRow) {
            crowdBatchTaskPending.setStop(true);
            log.info("messageTemplate:[{}] read csv file complete!", messageTemplateId);
        }
    }


}
