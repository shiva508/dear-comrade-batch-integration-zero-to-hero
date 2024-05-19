package com.comrade.config;

import com.comrade.entity.Event;
import com.comrade.service.ComradeService;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@AllArgsConstructor
public class DearComradeBatchConfig {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final ComradeService comradeService;

    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job dearComradeJob(){
        return new JobBuilder("dearComradeJob",jobRepository)
                .start(dearComradeStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }


    @Bean
    public Step dearComradeStep(){
      return new StepBuilder("dearComradeStep",jobRepository)
              .<Event,Event>chunk(12,platformTransactionManager)
              .reader(eventJpaPagingItemReader())
              .writer(stringItemWriter())
              .build();
    }

    @Bean
    public JpaPagingItemReader<Event> eventJpaPagingItemReader(){
        return new JpaPagingItemReaderBuilder<Event>()
                .name("eventJpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select event from Event event")
                .pageSize(10)
                .build();
    };

    @Bean
    public ItemWriter<Event> stringItemWriter(){
        return names -> names.forEach(System.out::println);
    }



}
