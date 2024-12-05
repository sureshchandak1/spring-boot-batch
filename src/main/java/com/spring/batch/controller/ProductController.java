package com.spring.batch.controller;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.batch.dtos.ApiRequest;
import com.spring.batch.dtos.product.ProductDto;
import com.spring.batch.dtos.product.ProductRequest;
import com.spring.batch.objects.ProductRequestStorage;

@RestController
@RequestMapping("/v1/api")
public class ProductController {

    private final Logger mLogger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRequestStorage productRequestStorage;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobOperator jobOperator;

    @PostMapping("/products")
    public ResponseEntity<String> saveProduct(@RequestBody ApiRequest<ProductRequest> requestBody) {

        // Check if the job is already running
        // JobInstance jobInstance = jobExplorer.getLastJobInstance("job");
        // if (jobInstance != null) {
        // try {
        // jobOperator.stop(jobInstance.getId());
        // } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
        // e.printStackTrace();
        // }
        // }
        // JobExecution runningJob = jobExplorer.findRunningJobExecutions("job")
        // .stream()
        // .findFirst()
        // .orElse(null);

        // if (runningJob != null) {
        // return ResponseEntity.status(HttpStatus.CONFLICT)
        // .body("Batch job is already running!");
        // }

        ProductRequest request = requestBody.getData();
        List<ProductDto> productList = request.getProductList();

        if (productList != null && !productList.isEmpty()) {
            productRequestStorage.setProductList(productList);

            try {

                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("timestamp", System.currentTimeMillis()) // Add unique parameter
                        .toJobParameters();

                JobExecution jobExecution = jobLauncher.run(job, jobParameters);

                return ResponseEntity.ok("Batch job started successfully!");

            } catch (Exception e) {
                mLogger.info("Exception: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Batch job failed: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Product list not be empty");
        }

    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopRunningJob() {
        try {

            Set<Long> runningJobIds = jobOperator.getRunningExecutions("job");
            for (Long jobId : runningJobIds) {
                jobOperator.stop(jobId);
            }
            return ResponseEntity.ok("All running jobs have been stopped!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to stop jobs: " + e.getMessage());
        }
    }

    private String getBatchId() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr.toUpperCase();

    }

}
