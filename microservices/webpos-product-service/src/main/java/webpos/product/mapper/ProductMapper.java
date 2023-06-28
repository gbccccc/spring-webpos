package webpos.product.mapper;

import webpos.product.pojo.Product;

import java.util.List;

public interface ProductMapper {
    List<Product> getProducts();

    Product getProductById(String id);
}
