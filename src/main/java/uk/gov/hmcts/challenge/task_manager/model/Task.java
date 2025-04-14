package uk.gov.hmcts.challenge.task_manager.model;

import java.time.LocalDateTime;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.challenge.task_manager.constants.constants.STATUS;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    private Long id;

    @Nonnull
    private String title;

    private String description;

    @Nonnull
    private LocalDateTime dueDate;

    @Nonnull
    private STATUS status;
}
