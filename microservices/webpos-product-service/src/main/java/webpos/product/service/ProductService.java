package webpos.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.product.database.ProductDB;
import webpos.product.pojo.Product;

import java.util.concurrent.atomic.AtomicInteger;


@Service
public class ProductService {
    private ProductDB productDB;

    @Autowired
    public void setProductsDB(ProductDB productDB) {
        this.productDB = productDB;
    }

    public Flux<Product> getProducts(int pageId, int numPerPage) {
        AtomicInteger index = new AtomicInteger(-1);
        return productDB.getProducts().filter(
                product -> {
                    index.getAndIncrement();
                    return pageId * numPerPage < index.get() && index.get() <= pageId * numPerPage + numPerPage;
                }
        );
    }

    public Mono<Product> getProductById(String productId) {
        return productDB.getProduct(productId);
    }
}
