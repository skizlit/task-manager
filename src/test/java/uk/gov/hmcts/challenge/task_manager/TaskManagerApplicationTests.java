package uk.gov.hmcts.challenge.task_manager;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class TaskManagerApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {

		assertThat(context).isNotNull();
		System.out.println("Context loaded");
	}
}
