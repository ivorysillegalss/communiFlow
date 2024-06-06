package org.chenzc.communi.constant;

import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

/**
 * Flink配置运行相关常量
 * @author chenz
 * @date 2024/06/06
 */
public class FlinkConstant {

    private FlinkConstant(){

    }

    /**
     * kafka相关配置信息
     * LogBroker需与application.properties文件中的配置相对应
     */
    public static final String LOG_GROUP_ID = "communiLogGroup";
    public static final String LOG_TOPIC_NAME = "communiTraceLog";
    public static final String LOG_BROKER = "communi-kafka:9092";


    /**
     * redis相关配置
     */
    public static final String REDIS_IP = "127.0.0.1";
    public static final String REDIS_PORT = "6379";
    public static final String REDIS_PASSWORD = "";

    public static final String SOURCE_NAME = "communi_kafka_source";
    public static final String TRACK_EVENT_TYPE_FUNCTION_NAME = "communi_transfer";
    public static final String SINK_NAME = "communi_sink";
    public static final String JOB_NAME = "CommuniBootStrap";

    public static final Integer MONTH = 30;
}
