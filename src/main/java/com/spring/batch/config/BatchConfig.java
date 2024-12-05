package com.spring.batch.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.spring.batch.dtos.product.ProductDto;
import com.spring.batch.objects.ProductRequestStorage;
import com.spring.batch.tables.ProductTable;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final ProductRequestStorage productRequestStorage;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobOperator jobOperator;

    public BatchConfig(ProductRequestStorage productRequestStorage) {
        this.productRequestStorage = productRequestStorage;
    }

    @Bean
    @StepScope
    @AfterStep
    public ListItemReader<ProductDto> readerBean() {
        List<ProductDto> productList = productRequestStorage.getProductList();
        if (productList == null) {
            productList = new ArrayList<>();
        }

        return new ListItemReader<>(productList);
    }

    @Bean
    public ItemProcessor<ProductDto, ProductTable> processorBean() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<ProductTable> writerBean() {
        return new CustomItemWriter();
    }

    @Bean
    public Step stepBean(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
            ItemReader<ProductDto> reader, ItemProcessor<ProductDto, ProductTable> processor,
            ItemWriter<ProductTable> writer) {
        return new StepBuilder("step", jobRepository)
                .<ProductDto, ProductTable>chunk(2, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job jobBean(JobRepository jobRepository, JobCompletionNotificationImpl listener, Step steps) {

        // JobExecution runningJob = jobExplorer.findRunningJobExecutions("job")
        // .stream()
        // .findFirst()
        // .orElse(null);

        // if (runningJob != null) {
        // jobRepository.deleteJobExecution(runningJob);
        // }

        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(steps)
                .build();
    }

}
