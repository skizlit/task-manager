package uk.gov.hmcts.challenge.task_manager;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class TaskManagerApplication {

	@Autowired
	ApplicationContext applicationContext;

	public static void main(String[] args) {

		SpringApplication.run(TaskManagerApplication.class, args);
	}

	// applicationContext.getId() is set by 'spring: application: name:' in the yaml config.
	@PostConstruct
	public void postConstruct() throws IOException {

		LOG.info("-------------- Starting {} --------------", applicationContext.getId());
	}
}
