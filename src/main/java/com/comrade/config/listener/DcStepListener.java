package com.comrade.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class DcStepListener implements StepExecutionListener {

    Date jobRunDate = null;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        jobRunDate = stepExecution.getJobExecution().getJobParameters().getDate("jobRunDate");
        log.info("beforeStep:: jobRunDate: {}", jobRunDate);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("afterStep:: afterStep: {}", jobRunDate);

        if (stepExecution.getStatus().equals(BatchStatus.FAILED)){
            System.out.println(stepExecution);
        }

        return StepExecutionListener.super.afterStep(stepExecution);
    }
}
