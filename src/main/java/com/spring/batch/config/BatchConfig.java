package com.spring.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.spring.batch.model.Product;
import com.spring.batch.tables.ProductTable;

@Configuration
public class BatchConfig {

    @Bean
    public Job jobBean(JobRepository jobRepository, JobCompletionNotificationImpl listener, Step steps) {
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(steps)
                .build();
    }

    @Bean
    public Step stepBean(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("jobStep", jobRepository)
                .<Product, ProductTable>chunk(5, transactionManager)
                .reader(readerBean())
                .processor(processorBean())
                .writer(writerBean())
                .build();
    }

    @Bean
    public FlatFileItemReader<Product> readerBean() {
        return new FlatFileItemReaderBuilder<Product>()
                .name("itemReader")
                .resource(new ClassPathResource("sample_data.xlsx"))
                .delimited()
                .names("productId", "title", "description", "price", "discount")
                .targetType(Product.class)
                .build();
    }

    @Bean
    public ItemProcessor<Product, ProductTable> processorBean() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<ProductTable> writerBean() {
        return new JpaItemWriterBuilder<ProductTable>().build();
    }

}
