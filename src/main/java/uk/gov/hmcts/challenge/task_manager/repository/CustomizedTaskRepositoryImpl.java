package uk.gov.hmcts.challenge.task_manager.repository;

import java.util.Optional;

import uk.gov.hmcts.challenge.task_manager.constants.constants.STATUS;
import uk.gov.hmcts.challenge.task_manager.model.Task;

public interface CustomizedTaskRepositoryImpl {

    // Update Methods
    Optional<Task> updateTaskStatus(STATUS status, Long id);
}