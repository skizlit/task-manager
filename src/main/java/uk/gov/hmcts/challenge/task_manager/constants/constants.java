package uk.gov.hmcts.challenge.task_manager.constants;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Slf4j
public class constants {

    public enum STATUS {
        PENDING,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        ONHOLD,
        DELAYED;

        public static STATUS getStatusFromValue(String value) {
            try {
                return STATUS.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException | NullPointerException ex) {
                LOG.error("Invalid status value: {}", value, ex);
                throw new IllegalArgumentException("Invalid status value: " + value);
            }
        }
    }
}
