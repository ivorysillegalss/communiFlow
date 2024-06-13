package org.chenzc.communi.entity.trace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.chenzc.communi.entity.SimpleTrackEventEntity;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class TraceResponse {
    private String code;

    private String message;

    private List<SimpleTrackEventEntity> data;
}
