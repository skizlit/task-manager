package uk.gov.hmcts.challenge.task_manager.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class constants {

    public enum STATUS {
        PENDING("Pending"),
        ASSIGNED("Assigned"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        ONHOLD("On Hold"),
        DELAYED("Delayed");

        private String value;

        STATUS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
