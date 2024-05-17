package com.comrade;

import com.comrade.entity.Event;
import com.comrade.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalTime;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@AllArgsConstructor
public class DearComradeBatchIntegrationZeroToHeroApplication {

	private final JobLauncher jobLauncher;

	private final Job dearComradeJob;

	private final EventRepository eventRepository;


	public static void main(String[] args) {
		SpringApplication.run(DearComradeBatchIntegrationZeroToHeroApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner(){


		return args -> {
			var events = IntStream.range(1,10).mapToObj(value -> Event.builder().title(String.format("Dasari_%s",value)).image(String.format("Shiva_%s",value)).date(LocalTime.now()).build()).collect(Collectors.toSet());
			eventRepository.saveAll(events);
			//jobLauncher.run(dearComradeJob,new JobParametersBuilder().addDate("jobRunDate",new Date()).toJobParameters());
		};
	}
}
