package com.example.batch.config;

import com.example.batch.model.Product;
import com.example.batch.service.JsonFileReader;
import com.example.batch.service.ProductProcessor;
import com.example.batch.service.ProductWriter;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableBatchProcessing
public class BatchConfig {


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean
    public ItemReader<JsonNode> itemReader() {
        return new JsonFileReader("data/products.json");
    }

    @Bean
    public ItemProcessor<JsonNode, Product> itemProcessor() {
        return new ProductProcessor();
    }

    @Bean
    public ItemWriter<Product> itemWriter() {
        return new ProductWriter();
    }

    @Bean
    protected Step processProducts(ItemReader<JsonNode> reader, ItemProcessor<JsonNode, Product> processor, ItemWriter<Product> writer) {
        return stepBuilderFactory.get("processProducts").<JsonNode, Product>chunk(1000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job chunksJob() {
        return jobBuilderFactory
                .get("chunksJob")
                .start(processProducts(itemReader(), itemProcessor(), itemWriter()))
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(20);
        return executor;
    }
}
