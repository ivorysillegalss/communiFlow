package org.chenzc.communi.receiver;

/**
 * 此接口是消费MQ接口
 * @author chenz
 * @date 2024/05/27
 */
public interface Receiver {
    /**
     * 消费MQ中信息方法
     * 暂时想不到好的方法去适配对应的实现类
     * @param args
     */
//    也许可以使用反射来适配 实现形参映射？ TODO
    void consume(Object... args);
}
