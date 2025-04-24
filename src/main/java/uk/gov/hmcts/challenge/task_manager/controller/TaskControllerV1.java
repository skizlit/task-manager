package uk.gov.hmcts.challenge.task_manager.controller;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.challenge.task_manager.constants.constants.STATUS;
import uk.gov.hmcts.challenge.task_manager.model.ApiCommonResponse;
import uk.gov.hmcts.challenge.task_manager.model.Task;
import uk.gov.hmcts.challenge.task_manager.repository.TaskRepository;

@Slf4j
@RestController
@RequestMapping(TaskControllerV1.TASK_V1_REQUEST_MAPPING)
public class TaskControllerV1 {

    protected static final String TASK_V1_REQUEST_MAPPING = "/api/v1/task";

    private final TaskRepository taskRepository;

    public TaskControllerV1(final TaskRepository taskRepository) {

        this.taskRepository = taskRepository;
    }

    @GetMapping
    public ResponseEntity<ApiCommonResponse> getAllTasks() {

        LOG.debug("Received: get all tasks");
        return ResponseEntity.ok(ApiCommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage("Success")
                .data(taskRepository.findAll())
                .build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiCommonResponse> findAllByStatus(@PathVariable String status) {

        LOG.debug("Received: get all tasks by status");

        try {

            return ResponseEntity.ok(ApiCommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("Success")
                    .data(taskRepository.findAllByStatus(STATUS.getStatusFromValue(status)))
                    .build());
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(ApiCommonResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusMessage("Bad Request - %s".formatted(e.getLocalizedMessage()))
                            .data(taskRepository.findAllByStatus(STATUS.getStatusFromValue(status)))
                            .build());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiCommonResponse> findById(@PathVariable Long id) {

        LOG.debug("Received: Get task by id: {}", id);
        if (id == null) {
            return ResponseEntity.badRequest()
                    .body(ApiCommonResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusMessage("Bad Request - Id cannot be null")
                            .build());
        }

        return ResponseEntity.ok(ApiCommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage("Success")
                .data(taskRepository.findById(id))
                .build());

    }

    @PutMapping()
    public ResponseEntity<ApiCommonResponse> createTask(@RequestBody Task task) {

        LOG.debug("Received: Create task: {}", task);
        if (task == null) {
            return ResponseEntity.badRequest()
                    .body(ApiCommonResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusMessage("Bad Request - Task cannot be null")
                            .build());
        }

        try {

            return ResponseEntity.ok(ApiCommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("Task Successfully Created")
                    .data(taskRepository.save(task))
                    .build());
        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(ApiCommonResponse.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusMessage("Internal Server Error - %s".formatted(e.getLocalizedMessage()))
                            .build());
        }
    }

    @PostMapping("/{id}/{status}")
    public ResponseEntity<ApiCommonResponse> updateTaskStatus(@PathVariable Long id,
            @PathVariable String status) {

        LOG.debug("Received: Update task: {}, status: {}", id, status);

        try {

            return ResponseEntity.ok(ApiCommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("Task Successfully Updated")
                    .data(taskRepository.updateTaskStatus(STATUS.getStatusFromValue(status), id))
                    .build());
        } catch (InvalidDataAccessApiUsageException | IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(ApiCommonResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .statusMessage("Invalid Argument - %s".formatted(e.getLocalizedMessage()))
                            .build());
        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body(ApiCommonResponse.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusMessage("Internal Server Error - %s".formatted(e.getLocalizedMessage()))
                            .build());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiCommonResponse> deleteTask(@PathVariable Long id) {

        LOG.debug("Received: Delete task: {}", id);

        taskRepository.deleteById(id);

        return ResponseEntity.ok(ApiCommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage("Task Successfully Deleted")
                .build());
    }
}
