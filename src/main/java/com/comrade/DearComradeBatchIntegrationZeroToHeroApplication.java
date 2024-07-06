package com.comrade;

import com.comrade.entity.TopicEntity;
import com.comrade.repository.TopicDetailsRepository;
import com.comrade.repository.TopicRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@AllArgsConstructor
@EnableScheduling
@Slf4j
public class DearComradeBatchIntegrationZeroToHeroApplication {

	private final TopicRepository topicRepository;
	private final TopicDetailsRepository topicDetailsRepository;


	public static void main(String[] args) {
		SpringApplication.run(DearComradeBatchIntegrationZeroToHeroApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner(){
		System.out.println(topicRepository.count());

		return args -> {
			if (false){
				topicDetailsRepository.deleteAll();
				topicRepository.deleteAll();
				var events = IntStream.range(1,10000).mapToObj(value -> TopicEntity.builder().title(String.format("Who won the world cup in the year %s ",value)).category("SPORTS").createdDate(LocalTime.now()).status("IN_PROGRESS").build()).collect(Collectors.toSet());
				topicRepository.saveAll(events);
				log.info("Completed");
				//jobLauncher.run(dearComradeJob,new JobParametersBuilder().addDate("jobRunDate",new Date()).toJobParameters());

			}
		};
	}
}
