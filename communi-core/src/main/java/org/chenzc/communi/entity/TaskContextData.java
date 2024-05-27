package org.chenzc.communi.entity;


import lombok.*;
import org.chenzc.communi.content.ContentModel;

import java.util.List;

/**
 * @author chenz
 * @date 2024/05/21
 * 任何业务流程中携带的数据的父类类型
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaskContextData extends ContentModel {
    private List<TaskInfo> taskInfos;
}
