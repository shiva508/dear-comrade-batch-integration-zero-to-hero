package com.comrade.config;

import com.comrade.config.listener.DcStepListener;
import com.comrade.config.reader.mapper.CustomerMapper;
import com.comrade.model.CustomerModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@EnableBatchProcessing
@Configuration
public class DearComradeBatchConfig {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    DcStepListener dcStepListener;


    @Bean
    public Step dcStep(){
        return new StepBuilder("dc-step", jobRepository)
                .<CustomerModel,CustomerModel>chunk(500,platformTransactionManager)
                .reader(customerModelPoiItemReader())
                .writer(customerModelItemWriter())
                .listener(dcStepListener)
                .build();
    }

    @Bean
    public Job dcJob(){
       return new JobBuilder("dc-job", jobRepository)
               .incrementer(new RunIdIncrementer())
               .start(dcStep())
               .build();
    }

    @Bean
    @StepScope
    public PoiItemReader<CustomerModel> customerModelPoiItemReader(){
        PoiItemReader<CustomerModel> reader = new PoiItemReader<>();
        reader.setResource(new FileSystemResource("C:\\Users\\dasar\\Downloads\\1-mb-error.xlsx"));
        reader.setRowMapper(customerMapper);
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public ItemWriter<CustomerModel> customerModelItemWriter(){
        return names -> names.forEach(customerModel -> {});
    }

    @Bean
    public DefaultJobParametersExtractor jobParametersExtractor() {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"error.message"});
        return extractor;
    }
}
