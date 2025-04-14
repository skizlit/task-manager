package uk.gov.hmcts.challenge.task_manager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import uk.gov.hmcts.challenge.task_manager.constants.constants.STATUS;
import uk.gov.hmcts.challenge.task_manager.model.Task;
import uk.gov.hmcts.challenge.task_manager.util.DatabaseTestUtil;

@DataJpaTest
class TaskRepositoryTests {

	@Autowired
	private TaskRepository taskRepository;

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
		void testFindAll() {

			List<Task> retreivedTaskList = taskRepository.findAll();

			assertEquals(3, retreivedTaskList.size());
			assertTrue(retreivedTaskList.stream()
					.allMatch(d -> d.getTitle().equals(taskList.get(0).getTitle())
							|| d.getTitle().equals(taskList.get(1).getTitle())
							|| d.getTitle().equals(taskList.get(2).getTitle())));
		}

		@Test
		void testFindAllByStatus() {

			List<Task> retreivedTaskList = taskRepository.findAllByStatus(STATUS.IN_PROGRESS);

			assertEquals(2, retreivedTaskList.size());
			assertTrue(retreivedTaskList.stream()
					.allMatch(d -> d.getTitle().equals(taskList.get(1).getTitle())
							|| d.getTitle().equals(taskList.get(2).getTitle())));
		}

		@Test
		void testFindAllByStatus_NoRecordsExistForStatus() {

			List<Task> retreivedTaskList = taskRepository.findAllByStatus(STATUS.COMPLETED);

			assertTrue(retreivedTaskList.isEmpty());
		}

		@Test
		void testFindById() {

			Optional<Task> foundTaskOpt = taskRepository.findById(taskList.get(0).getId());

			assertTrue(foundTaskOpt.isPresent());
			assertEquals(taskList.get(0).getId(), foundTaskOpt.get().getId());
			assertEquals(taskList.get(0).getTitle(), foundTaskOpt.get().getTitle());
			assertEquals(taskList.get(0).getDescription(), foundTaskOpt.get().getDescription());
			assertEquals(taskList.get(0).getDueDate(), foundTaskOpt.get().getDueDate());
			assertEquals(taskList.get(0).getStatus(), foundTaskOpt.get().getStatus());
		}

		@Test
		void testFindById_NoRecordExistsForId() {

			Optional<Task> foundTaskOpt = taskRepository.findById(9999l);

			assertFalse(foundTaskOpt.isPresent());
		}
	}

	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class Update {

		@Test
		void testUpdateRecord() {

			Optional<Task> foundTaskOpt = taskRepository.findById(taskList.get(0).getId());

			Task taskToBeUpdated = foundTaskOpt.get();
			taskToBeUpdated.setTitle("Updated Title");
			taskToBeUpdated.setStatus(STATUS.COMPLETED);

			taskRepository.save(taskToBeUpdated);

			foundTaskOpt = taskRepository.findById(taskList.get(0).getId());
			assertTrue(foundTaskOpt.isPresent());
			assertEquals(taskList.get(0).getId(), foundTaskOpt.get().getId());
			assertEquals("Updated Title", foundTaskOpt.get().getTitle());
			assertEquals(taskList.get(0).getDescription(), foundTaskOpt.get().getDescription());
			assertEquals(taskList.get(0).getDueDate(), foundTaskOpt.get().getDueDate());
			assertEquals(STATUS.COMPLETED, foundTaskOpt.get().getStatus());
		}

		@Test
		void testUpdate_NullPointerExceptionTitle() {

			Optional<Task> foundTaskOpt = taskRepository.findById(taskList.get(0).getId());

			Task taskToBeUpdated = foundTaskOpt.get();

			assertThrows(NullPointerException.class, () -> {
				taskToBeUpdated.setTitle(null);
			});
		}

		@Test
		void testUpdateTaskStatus() {

			taskRepository.updateTaskStatus(STATUS.COMPLETED, taskList.get(0).getId());

			Optional<Task> updatedTaskOpt = taskRepository.findById(taskList.get(0).getId());
			assertTrue(updatedTaskOpt.isPresent());
			assertEquals(taskList.get(0).getId(), updatedTaskOpt.get().getId());
			assertEquals(taskList.get(0).getTitle(), updatedTaskOpt.get().getTitle());
			assertEquals(taskList.get(0).getDescription(), updatedTaskOpt.get().getDescription());
			assertEquals(taskList.get(0).getDueDate(), updatedTaskOpt.get().getDueDate());
			assertEquals(STATUS.COMPLETED, updatedTaskOpt.get().getStatus());
		}
	}

	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class Delete {

		@Test
		void testDeleteRecord() {

			Optional<Task> foundTaskOpt = taskRepository.findById(taskList.get(0).getId());
			assertTrue(foundTaskOpt.isPresent());

			taskRepository.deleteById(taskList.get(0).getId());

			foundTaskOpt = taskRepository.findById(taskList.get(0).getId());
			assertFalse(foundTaskOpt.isPresent());
		}
	}
}
