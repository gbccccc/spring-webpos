package com.example.batch.service;

import com.example.batch.model.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

public class ProductProcessor implements ItemProcessor<JsonNode, Product>, StepExecutionListener {

    private ObjectMapper objectMapper;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public Product process(JsonNode jsonNode) throws Exception {
        return objectMapper.treeToValue(jsonNode, Product.class);
    }
}
