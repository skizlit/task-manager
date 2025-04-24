package uk.gov.hmcts.challenge.task_manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiCommonResponse {

    private int statusCode;
    private String statusMessage;
    private Object data;
}
