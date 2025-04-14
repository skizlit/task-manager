package uk.gov.hmcts.challenge.task_manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import uk.gov.hmcts.challenge.task_manager.constants.constants.STATUS;
import uk.gov.hmcts.challenge.task_manager.model.Task;

public interface TaskRespostory extends CrudRepository<Task, Long> {

    // Find Methods
    public List<Task> findAll();
    public List<Task> findAllByStatus(STATUS Status);
    public Optional<Task> findById(Long id);
}
