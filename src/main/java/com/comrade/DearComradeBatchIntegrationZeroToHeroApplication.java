package com.comrade;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class DearComradeBatchIntegrationZeroToHeroApplication {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job dcJob;

	public static void main(String[] args) {
		SpringApplication.run(DearComradeBatchIntegrationZeroToHeroApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner(){
		return args -> {
			JobParameters jobRunDate = new JobParametersBuilder().addDate("jobRunDate", new Date()).toJobParameters();
			jobLauncher.run(dcJob, jobRunDate);
		};
	}

}
