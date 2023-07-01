package webpos.product.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.product.pojo.Product;
import webpos.product.service.ProductService;

import java.util.logging.Logger;

@Slf4j
@RestController
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public Flux<Product> allProducts() {
        return service.getProducts();
    }

    @GetMapping("/products/{productId}")
    public Mono<Product> product(@PathVariable String productId) {
        return service.getProductById(productId);
    }
}
