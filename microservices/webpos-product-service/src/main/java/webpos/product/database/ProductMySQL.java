package webpos.product.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Component;
import webpos.product.mapper.ProductMapper;
import webpos.product.pojo.Product;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMySQL implements ProductDB {
    private SqlSessionFactory sqlSessionFactory;

    public ProductMySQL() {
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
    public List<Product> getProducts() {
        List<Product> products = null;
        try (SqlSession sqlSession = getSqlSession()) {
            ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
            products = mapper.getProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products == null ? new ArrayList<>() : products;
    }

    @Override
    public Product getProduct(String productId) {
        Product product = null;
        try (SqlSession sqlSession = getSqlSession()) {
            ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
            product = mapper.getProductById(productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }
}
