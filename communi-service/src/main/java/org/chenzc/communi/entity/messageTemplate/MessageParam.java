package org.chenzc.communi.entity.messageTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Data
@Getter
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class MessageParam {

    /**
     * 接受者 可以同时有多个接受者 （群发 & 单发）
     */
    private Set<String> receiver;

    /**
     * 替换消息模板中占位符的内容
     */
    private List<String> variables;
}
