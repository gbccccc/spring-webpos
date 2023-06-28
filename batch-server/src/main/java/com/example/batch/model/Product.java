package com.example.batch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    private String main_cat;

    private String title;

    private String asin;

    private List<String> category;

    private List<String> imageURLHighRes;
}
