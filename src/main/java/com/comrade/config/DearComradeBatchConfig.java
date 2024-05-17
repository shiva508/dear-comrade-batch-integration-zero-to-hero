package com.comrade.config;

import com.comrade.service.ComradeService;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
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

    @Bean
    public Job dearComradeJob(){
        return new JobBuilder("dearComradeJob",jobRepository)
                .start(dearComradeStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }


    @Bean
    @StepScope
    public Step dearComradeStep(){
      return new StepBuilder("dearComradeStep",jobRepository)
              .<List<String>,List<String>>chunk(12,platformTransactionManager)
              .reader(stringItemReaderAdapter())
              .writer(stringItemWriter())
              .build();
    }

    @Bean
    public ItemReaderAdapter<List<String>> stringItemReaderAdapter(){
        ItemReaderAdapter<List<String>> stringItemReaderAdapter = new ItemReaderAdapter<>();
        stringItemReaderAdapter.setTargetObject(comradeService);
        stringItemReaderAdapter.setTargetMethod("names");
        return stringItemReaderAdapter;
    }

    @Bean
    public ItemWriter<List<String>> stringItemWriter(){
        return names -> names.forEach(System.out::println);
    }


}
