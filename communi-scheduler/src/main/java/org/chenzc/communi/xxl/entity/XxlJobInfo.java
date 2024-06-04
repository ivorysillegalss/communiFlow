package org.chenzc.communi.xxl.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * xxl任务信息的配置
 * @author chenz
 * @date 2024/06/03
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class XxlJobInfo implements Serializable {

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 执行器主键ID 及 描述
     */
    private int jobGroup;
    private String jobDesc;

    private Date addTime;
    private Date updateTime;

    /**
     * 负责人
     */
    private String author;

    /**
     * 报警邮件
     */
    private String alarmEmail;

    /**
     * 调度类型
     */
    private String scheduleType;

    /**
     * 调度配置 值含义取决于调度类型
     */
    private String scheduleConf;

    /**
     * 调度过期策略
     */
    private String misfireStrategy;

    /**
     * 执行器路由配置
     */
    private String executorRouteStrategy;

    /**
     * 任务handler名称 （执行器名称）
     */
    private String executorHandler;

    /**
     * 执行器的任务参数
     */
    private String executorParam;

    /**
     * 阻塞处理策略
     */
    private String executorBlockStrategy;

    /**
     * 任务执行的超时时间
     */
    private int executorTimeout;

    /**
     * 失败重试次数
     */
    private int executorFailRetryCount;

    /**
     * GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
     */
    private String glueType;

    /**
     * GLUE源代码
     */
    private String glueSource;

    /**
     * GLUE备注
     */
    private String glueRemark;

    /**
     * GLUE更新时间
     */
    private Date glueUpdateTime;

    /**
     * 子任务ID，多个逗号分隔
     */
    private String childJobId;

    /**
     * 调度状态：0-停止，1-运行
     */
    private int triggerStatus;

    /**
     * 上次调度时间
     */
    private long triggerLastTime;

    /**
     * 下次调度时间
     */
    private long triggerNextTime;
}
