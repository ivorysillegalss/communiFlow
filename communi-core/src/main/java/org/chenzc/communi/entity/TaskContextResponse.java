package org.chenzc.communi.entity;

import lombok.Builder;
import lombok.Getter;

/** 责任链模式响应类 表示责任链运行的结果
 * @author chenz
 * @date 2024/05/21
 * */
@Builder
@Getter
public class TaskContextResponse<T> {
    /**
     * 业务码 可以优化为枚举类 （执行到哪一个状态）
     */
    private String code;

    /**
     * 返回的数据
     */
    private T data;
}
