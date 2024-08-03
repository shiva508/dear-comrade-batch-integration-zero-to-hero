package com.comrade.config;

import javax.sql.DataSource;

import com.comrade.entity.db.old.StudentOld;
import com.comrade.entity.db.latest.StudentLatest;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class StudentpoolBatchConfig {

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	@Qualifier("datasourceOld")
	private DataSource datasourceOld;

	@Autowired
	@Qualifier("datasourceLatest")
	private DataSource datasourceLatest;

	@Autowired
	@Qualifier("datasourceLatestEntityManagerFactory")
	private EntityManagerFactory datasourceLatestEntityManagerFactory;

	@Autowired
	@Qualifier("datasourceOldEntityManagerFactory")
	private EntityManagerFactory datasourceOldEntityManagerFactory;
	
	@Autowired
	private StudentItemProcessor studentItemProcessor;
	
	@Autowired
	private JpaTransactionManager jpaTransactionManager;

	@Bean
	public Job jpaBatchJob( ) {
		return new JobBuilder("Jpa Batch Job",jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(jpaBatchStep())
				.build();
	}

	
	public Step jpaBatchStep() {
		return new StepBuilder("Jpa Batch Step",jobRepository)
								.<StudentOld, StudentLatest>chunk(2,transactionManager)
								.reader(jpaCursorItemReader())
								.processor(studentItemProcessor)
								.writer(jpaItemWriter())
								.transactionManager(jpaTransactionManager)
								.build();
	}

	public JpaCursorItemReader<StudentOld> jpaCursorItemReader() {
		JpaCursorItemReader<StudentOld> jpaCursorItemReader = new JpaCursorItemReader<>();
		jpaCursorItemReader.setEntityManagerFactory(datasourceOldEntityManagerFactory);
		jpaCursorItemReader.setQueryString("FROM StudentOld");
		return jpaCursorItemReader;
	}

	public JpaItemWriter<StudentLatest> jpaItemWriter() {
		JpaItemWriter<StudentLatest> jpaItemWriter=new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(datasourceLatestEntityManagerFactory);
		return jpaItemWriter;
	}
}
