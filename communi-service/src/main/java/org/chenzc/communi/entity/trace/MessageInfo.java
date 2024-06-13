package org.chenzc.communi.entity.trace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfo {

    private Long businessId;

    private String title;

    private String sendType;

    private String creator;
}
