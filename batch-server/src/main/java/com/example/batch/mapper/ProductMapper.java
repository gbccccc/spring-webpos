package com.example.batch.mapper;

import com.example.batch.model.Product;

import java.util.List;

public interface ProductMapper {
    void insertProductBasic(Product product);

    void insertCategories(String asin, List<String> categories);

    void insertImagesURLHighres(String asin, List<String> imagesURL);

    List<Product> getProducts();

    Product getProductById(String id);
}
