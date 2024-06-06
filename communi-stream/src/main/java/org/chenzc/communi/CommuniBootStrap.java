package org.chenzc.communi;


import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.chenzc.communi.constant.FlinkConstant;
import org.chenzc.communi.entity.TrackEventEntity;
import org.chenzc.communi.enums.TrackEventType;
import org.chenzc.communi.function.EventTypeTransformation;
import org.chenzc.communi.sink.RedisSink;
import org.chenzc.communi.utils.MessageQueueUtils;

/**
 * flink启动类
 * <p></>
 * 在flink中 数据处理的流程可分为三个步骤：
 * 创建数据源 Source 从外部系统读取数据
 * 业务处理逻辑 Transformation 对读取进来的数据进行各种转换和处理操作
 * 数据持久化 Sink 将处理后的数据写入外部系统
 *
 * @author chenz
 * @date 2024/06/06
 */
@Slf4j
public class CommuniBootStrap {


    /**
     * 对于此中间 个人认为可以理解成 enhancer or filter or channel
     * 其意义就是 加工外部数据后 持久化
     *
     * @param args
     */
    public static void main(String[] args) {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

//        1. 获取source
//        获取kafkaConsumer
        KafkaSource<String> kafkaConsumer = MessageQueueUtils.getKafkaConsumer(
                FlinkConstant.LOG_TOPIC_NAME, FlinkConstant.LOG_GROUP_ID, FlinkConstant.LOG_BROKER);

        DataStreamSource<String> dataSource = executionEnvironment.fromSource(kafkaConsumer,
                WatermarkStrategy.noWatermarks(), FlinkConstant.SOURCE_NAME);


//        2. 经过Transformation对数据进行操作
        SingleOutputStreamOperator<TrackEventEntity> outputDataSource = dataSource.flatMap(new EventTypeTransformation())
                .name(FlinkConstant.TRACK_EVENT_TYPE_FUNCTION_NAME);

//        3. 将处理后的数据持久化入Redis当中
        outputDataSource.addSink(new RedisSink()).name(FlinkConstant.SINK_NAME);

        try {
            executionEnvironment.execute(FlinkConstant.JOB_NAME);
        }catch (Exception e){
            log.error("flink word log error!");
        }
    }


}
