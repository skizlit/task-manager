package uk.gov.hmcts.challenge.task_manager.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import uk.gov.hmcts.challenge.task_manager.constants.constants.STATUS;
import uk.gov.hmcts.challenge.task_manager.model.Task;
import uk.gov.hmcts.challenge.task_manager.repository.TaskRepository;

@Getter
public class DatabaseTestUtil {

    private final TaskRepository taskRepository;
    private List<Task> taskList = new ArrayList<>();

    public DatabaseTestUtil(final TaskRepository taskRepository) {

        this.taskRepository = taskRepository;

        taskRepository.deleteAll();
        createTestTasks();
    }

    private void createTestTasks() {
        taskList.add(taskRepository.save(buildTask("Test Task 1", "Description for task 1", LocalDateTime.now().plusDays(1), STATUS.PENDING)));
        taskList.add(taskRepository.save(buildTask("Test Task 2", "Description for task 2", LocalDateTime.now().plusDays(2), STATUS.IN_PROGRESS)));
        taskList.add(taskRepository.save(buildTask("Test Task 3", null, LocalDateTime.now(), STATUS.IN_PROGRESS)));
    }

    private Task buildTask(String title, String description, LocalDateTime dueDate, STATUS status) {
        return Task.builder()
                .title(title)
                .description(description)
                .dueDate(dueDate)
                .status(status)
                .build();
    }
}
