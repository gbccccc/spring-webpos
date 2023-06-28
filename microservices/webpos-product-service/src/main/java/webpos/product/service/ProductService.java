package webpos.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webpos.product.database.ProductDB;
import webpos.product.pojo.Product;

import java.util.List;

@Service
public class ProductService {
    private ProductDB productDB;

    @Autowired
    public void setProductsDB(ProductDB productDB) {
        this.productDB = productDB;
    }

    public List<Product> getProducts() {
        return productDB.getProducts();
    }

    public Product getProductById(String productId) {
        return productDB.getProduct(productId);
    }
}
