package webpos.product.database;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.product.pojo.Product;

import java.util.List;

public interface ProductDB {
    Flux<Product> getProducts();

    Mono<Product> getProduct(String productId);
}
