package com.example.batch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonFileReader implements StepExecutionListener, ItemReader<JsonNode> {

    private BufferedReader reader;

    private ObjectMapper objectMapper;

    private String fileName;

    public JsonFileReader(String file) {
        if (file.matches("^file:(.*)"))
            file = file.substring(file.indexOf(":") + 1);
        this.fileName = file;
    }

    private void initReader() throws FileNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        reader = new BufferedReader(new FileReader(file));
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public synchronized JsonNode read() throws Exception {
        if (objectMapper == null)
            objectMapper = new ObjectMapper();

        if (reader == null) {
            initReader();
        }

        String line = reader.readLine();

        if (line != null)
            return objectMapper.readTree(line);
        else
            return null;
    }
}
