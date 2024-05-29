package org.chenzc.communi.task.deduplication.limit;

import org.chenzc.communi.task.deduplication.entity.DeduplicationEntity;
import org.chenzc.communi.task.deduplication.service.AbstractDeduplicationService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SimpleLimitDeduplicationService implements LimitDeduplicationService{

    @Override
    public Set<String> doLimit(AbstractDeduplicationService service, DeduplicationEntity entity) {
//        TODO
        return new HashSet<>();
    }
}
