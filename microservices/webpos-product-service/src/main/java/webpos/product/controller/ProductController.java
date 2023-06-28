package webpos.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import webpos.product.pojo.Product;
import webpos.product.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> allProducts() {
        List<Product> products = service.getProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> product(@PathVariable String productId) {
        Product product = service.getProductById(productId);
        return product == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(product);
    }
}
