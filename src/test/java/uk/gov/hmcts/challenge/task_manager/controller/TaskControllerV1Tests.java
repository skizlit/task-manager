package uk.gov.hmcts.challenge.task_manager.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.gov.hmcts.challenge.task_manager.constants.constants.STATUS;
import uk.gov.hmcts.challenge.task_manager.model.Task;
import uk.gov.hmcts.challenge.task_manager.repository.TaskRepository;
import uk.gov.hmcts.challenge.task_manager.util.DatabaseTestUtil;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerV1Tests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private List<Task> taskList = new ArrayList<>();

	@BeforeEach
	void setup() {
		DatabaseTestUtil databaseTestUtil = new DatabaseTestUtil(taskRepository);
		taskList = databaseTestUtil.getTaskList();
	}

	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class Find {

		@Test
		void testGetAllTasks() throws Exception {
			mockMvc.perform(get(TaskControllerV1.TASK_V1_REQUEST_MAPPING))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.statusCode").value(200))
					.andExpect(jsonPath("$.data", hasSize(3)));
		}

		@Test
		void testFindAllByStatus() throws Exception {
			mockMvc.perform(get(String.format("%s/status/%s", TaskControllerV1.TASK_V1_REQUEST_MAPPING, taskList.get(1).getStatus())))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.statusCode").value(200))
					.andExpect(jsonPath("$.data", hasSize(2)))
					.andExpect(jsonPath("$.data[0].status").value(taskList.get(1).getStatus().toString()))
					.andExpect(jsonPath("$.data[0].id").value(taskList.get(1).getId()))
					.andExpect(jsonPath("$.data[1].id").value(taskList.get(2).getId()));
		}

		@Test
		void testFindById() throws Exception {
			mockMvc.perform(
					get(String.format("%s/id/%s", TaskControllerV1.TASK_V1_REQUEST_MAPPING, taskList.get(0).getId())))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.statusCode").value(200))
					.andExpect(jsonPath("$.data.id").value(taskList.get(0).getId()));
		}

		@Test
		void testFindById_NoTaskFoundForID() throws Exception {
			mockMvc.perform(get(String.format("%s/id/%s", TaskControllerV1.TASK_V1_REQUEST_MAPPING, 99999)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.statusCode").value(200));
		}
	}

	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class Create {

		@Test
		void testCreateTask() throws Exception {
			Task newTask = new Task();
			newTask.setTitle("New Task");
			newTask.setStatus(STATUS.IN_PROGRESS);
			newTask.setDueDate(LocalDateTime.now().plusDays(2));

			mockMvc.perform(put(TaskControllerV1.TASK_V1_REQUEST_MAPPING)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(newTask)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.data.title").value("New Task"));
		}
	}

	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class Update {

		@Test
		void testUpdateTaskStatus() throws Exception {
			mockMvc.perform(post(String.format("%s/%s/%s", TaskControllerV1.TASK_V1_REQUEST_MAPPING, taskList.get(0).getId(), "COMPLETED")))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.statusMessage").value("Task Successfully Updated"));

			Optional<Task> updated = taskRepository.findById(taskList.get(0).getId());
			assert updated.isPresent();
			assert updated.get().getStatus() == STATUS.COMPLETED;
		}

		@Test
		void testUpdateTaskStatus_InvalidTaskId() throws Exception {
			mockMvc.perform(
					post(String.format("%s/%s/%s", TaskControllerV1.TASK_V1_REQUEST_MAPPING, 99999, "COMPLETED")))
					.andExpect(status().is4xxClientError())
					.andExpect(jsonPath("$.statusMessage").value("Invalid Argument - Task with id {99999} not found"));
		}

		@Test
		void testUpdateTaskStatus_InvalidTaskStatus() throws Exception {
			mockMvc.perform(
					post(String.format("%s/%s/%s", TaskControllerV1.TASK_V1_REQUEST_MAPPING, taskList.get(0).getId(), "UNKNOWN")))
					.andExpect(status().is4xxClientError())
					.andExpect(jsonPath("$.statusMessage").value("Invalid Argument - Invalid status value: UNKNOWN"));
		}
	}

	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class Delete {

		@Test
		void testDeleteTask() throws Exception {
			mockMvc.perform(delete(String.format("%s/%s", TaskControllerV1.TASK_V1_REQUEST_MAPPING, taskList.get(0).getId())))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.statusMessage").value("Task Successfully Deleted"));
		}

		@Test
		void testDeleteTask_UnknownId() throws Exception {
			mockMvc.perform(delete(String.format("%s/%s", TaskControllerV1.TASK_V1_REQUEST_MAPPING, 9999)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.statusMessage").value("Task Successfully Deleted"));
		}
	}
}
