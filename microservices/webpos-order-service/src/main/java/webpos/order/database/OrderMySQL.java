package webpos.order.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Repository;
import webpos.order.mapper.OrderMapper;
import webpos.order.pojo.Order;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderMySQL implements OrderDB {
    private SqlSessionFactory sqlSessionFactory;

    public OrderMySQL() {
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
    public List<Order> getAllOrders() {
        List<Order> orders = null;
        try (SqlSession sqlSession = getSqlSession()) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            orders = mapper.getAllOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders == null ? new ArrayList<>() : orders;
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        List<Order> orders = null;
        try (SqlSession sqlSession = getSqlSession()) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            orders = mapper.getOrdersByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders == null ? new ArrayList<>() : orders;
    }

    @Override
    public Order getOrder(String orderId) {
        Order order = null;
        try (SqlSession sqlSession = getSqlSession()) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            order = mapper.getOrder(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public boolean addOrder(Order order) {
        boolean success = false;
        try (SqlSession sqlSession = getSqlSession()) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            mapper.insertOrder(order);
            sqlSession.commit();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public int getOrderNum() {
        int num = 0;
        try (SqlSession sqlSession = getSqlSession()) {
            OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
            num = mapper.getOrderNum();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }
}
