package webpos.order.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.order.mapper.OrderMapper;
import webpos.order.pojo.Order;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Repository("MyBatis")
@ConditionalOnProperty(value = "spring.repository.type", havingValue = "MyBatis")
public class OrderMyBatis implements OrderDB {
    private SqlSessionFactory sqlSessionFactory;

    public OrderMyBatis() {
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
    public Flux<Order> getAllOrders() {
        return Flux.defer(
                () -> {
                    List<Order> orders = null;
                    try (SqlSession sqlSession = getSqlSession()) {
                        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
                        orders = mapper.getAllOrders();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return orders == null ? Flux.empty() : Flux.fromIterable(orders);
                }
        );
    }

    @Override
    public Flux<Order> getOrdersByUserId(String userId) {
        return Flux.defer(
                () -> {
                    List<Order> orders = null;
                    try (SqlSession sqlSession = getSqlSession()) {
                        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
                        orders = mapper.getOrdersByUserId(userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return orders == null ? Flux.empty() : Flux.fromIterable(orders);
                }
        );
    }

    @Override
    public Mono<Order> getOrder(String orderId) {
        return Mono.defer(
                () -> {
                    Order order = null;
                    try (SqlSession sqlSession = getSqlSession()) {
                        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
                        order = mapper.getOrder(orderId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Mono.just(order);
                }
        );
    }

    @Override
    public Mono<Order> addOrder(Order order) {
        return Mono.defer(
                () -> {
                    boolean success = false;
                    try (SqlSession sqlSession = getSqlSession()) {
                        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
                        mapper.insertOrder(order);
                        sqlSession.commit();
                        success = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return success ? Mono.just(order) : Mono.empty();
                }
        );
    }

    @Override
    public Mono<Integer> getOrderNum() {

        return Mono.defer(
                () -> {
                    int num = 0;
                    try (SqlSession sqlSession = getSqlSession()) {
                        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
                        num = mapper.getOrderNum();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Mono.just(num);
                }
        );
    }
}