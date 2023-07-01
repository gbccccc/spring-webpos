package webpos.product.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.product.pojo.Category;
import webpos.product.pojo.ImageUrl;
import webpos.product.pojo.Product;
import webpos.product.pojo.SimpleProduct;

import java.util.List;

@Repository("R2DBC")
@ConditionalOnProperty(value = "spring.repository.type", havingValue = "R2DBC")
public class ProductR2DBC implements ProductDB{
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    public void setR2dbcEntityTemplate(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Flux<Product> getProducts() {
        Flux<SimpleProduct> simpleProductFlux = r2dbcEntityTemplate.select(SimpleProduct.class).from("products").all();
        return assemblyProducts(simpleProductFlux);
    }

    @Override
    public Mono<Product> getProduct(String productId) {
        Flux<SimpleProduct> simpleProductFlux = r2dbcEntityTemplate.select(SimpleProduct.class).from("products")
                .matching(Query.query(Criteria.where("id").is(productId)))
                .all();

        return assemblyProducts(simpleProductFlux).next();
    }

    private Flux<Product> assemblyProducts(Flux<SimpleProduct> simpleProductFlux){
        Flux<List<String>> categoriesFlux = simpleProductFlux.flatMap(
                simpleProduct -> r2dbcEntityTemplate.select(Category.class).from("category")
                        .matching(Query.query(Criteria.where("id").is(simpleProduct.getAsin())))
                        .all().map(Category::getCategory).collectList()
        );
        Flux<List<String>> imagesFlux = simpleProductFlux.flatMap(
                simpleProduct -> r2dbcEntityTemplate.select(ImageUrl.class).from("image_url_highres")
                        .matching(Query.query(Criteria.where("id").is(simpleProduct.getAsin())))
                        .all().map(ImageUrl::getImage).collectList()
        );

        return simpleProductFlux.zipWith(categoriesFlux,
                (simpleProduct, categories) -> {
                    Product product = new Product();
                    product.setAsin(simpleProduct.getAsin());
                    product.setTitle(simpleProduct.getTitle());
                    product.setMain_cat(simpleProduct.getMain_cat());
                    product.setCategory(categories);
                    return product;
                }
        ).zipWith(imagesFlux,
                (product, images) -> {
                    product.setImageURLHighRes(images);
                    return product;
                }
        );
    }
}
