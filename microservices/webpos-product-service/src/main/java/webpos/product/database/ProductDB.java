package webpos.product.database;

import webpos.product.pojo.Product;

import java.util.List;

public interface ProductDB {
    List<Product> getProducts();

    Product getProduct(String productId);
}
