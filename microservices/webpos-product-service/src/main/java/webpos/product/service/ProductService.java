package webpos.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.product.database.ProductDB;
import webpos.product.pojo.Product;


@Service
public class ProductService {
    private ProductDB productDB;

    @Autowired
    public void setProductsDB(ProductDB productDB) {
        this.productDB = productDB;
    }

    public Flux<Product> getProducts() {
        return productDB.getProducts();
    }

    public Mono<Product> getProductById(String productId) {
        return productDB.getProduct(productId);
    }
}
