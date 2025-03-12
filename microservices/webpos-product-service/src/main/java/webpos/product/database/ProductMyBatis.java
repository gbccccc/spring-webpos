package webpos.product.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.product.mapper.ProductMapper;
import webpos.product.pojo.Product;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository("MyBatis")
@ConditionalOnProperty(value = "spring.repository.type", havingValue = "MyBatis")
public class ProductMyBatis implements ProductDB {
    private SqlSessionFactory sqlSessionFactory;

    public ProductMyBatis() {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }

    @Override
    public Flux<Product> getProducts(int pageId, int numPerPage) {
        return Flux.defer(
                () -> {
                    List<Product> products = null;
                    try (SqlSession sqlSession = getSqlSession()) {
                        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
                        products = mapper.getProducts();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    List<Product> finalProducts = products;
                    return finalProducts == null ? Flux.empty() : Flux.fromIterable(finalProducts);
                }
        ).take((long) pageId * numPerPage + numPerPage)
                .skip((long) pageId * numPerPage);
    }

    @Override
    public Mono<Product> getProduct(String productId) {
        return Mono.defer(
                () -> {
                    Product product = null;
                    try (SqlSession sqlSession = getSqlSession()) {
                        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
                        product = mapper.getProductById(productId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Product finalProduct = product;

                    return finalProduct == null ? Mono.empty() : Mono.just(finalProduct);
                }
        );
    }
}
