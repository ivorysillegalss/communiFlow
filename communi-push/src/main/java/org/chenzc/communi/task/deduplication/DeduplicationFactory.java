package org.chenzc.communi.task.deduplication;


import org.chenzc.communi.task.deduplication.builder.DeduplicationBuilder;
import org.chenzc.communi.task.deduplication.service.DeduplicationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 工厂类 维护 去重相关DTO & 去重相关服务
 *
 * @author chenz
 * @date 2024/05/28
 */
@Service
public class DeduplicationFactory {

    /**
     * 维护构建 去重参数DTO 的MAP
     */
    private final Map<String, DeduplicationBuilder> deduplicationBuilders = new HashMap<>();

    /**
     * 维护构建 去重服务 的MAP
     */
    private final Map<String, DeduplicationService> deduplicationServices = new HashMap<>();

    public DeduplicationBuilder newBuilderInstance(String deduplicationType) {
        return deduplicationBuilders.get(deduplicationType);
    }

    public DeduplicationService newServiceInstance(String deduplicationType) {
        return deduplicationServices.get(deduplicationType);
    }

    public void putBuilder(String deduplicationType, DeduplicationBuilder builder) {
        deduplicationBuilders.put(deduplicationType, builder);
    }

    public void putService(String deduplicationType, DeduplicationService service) {
        deduplicationServices.put(deduplicationType, service);
    }
}
