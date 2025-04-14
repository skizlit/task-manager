package uk.gov.hmcts.challenge.task_manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.hmcts.challenge.task_manager.constants.constants.STATUS;
import uk.gov.hmcts.challenge.task_manager.model.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>, CustomizedTaskRepositoryImpl {

    // Find Methods
    public List<Task> findAll();

    public List<Task> findAllByStatus(STATUS Status);

    public Optional<Task> findById(Long id);

    // Update Methods
    @Transactional
    public default Optional<Task> updateTaskStatus(STATUS status, Long id) {

        Task task = findById(id).orElseThrow(() -> new IllegalArgumentException("Task with id {" + id + "} not found"));

        task.setStatus(status);
        save(task);

        return findById(id);
    }
}
