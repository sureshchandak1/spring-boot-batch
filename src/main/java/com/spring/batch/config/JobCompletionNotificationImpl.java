package com.spring.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationImpl implements JobExecutionListener {

    private final Logger mLogger = LoggerFactory.getLogger(JobCompletionNotificationImpl.class);

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        mLogger.info("Job Started");
    }

    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            mLogger.info("Job Completed");
        }
    }

}
