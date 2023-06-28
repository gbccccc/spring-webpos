package delivery.database;

import delivery.mapper.DeliveryMapper;
import delivery.pojo.Delivery;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DeliveryMySQL implements DeliveryDB {
    private SqlSessionFactory sqlSessionFactory;

    public DeliveryMySQL() {
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
    public List<Delivery> getAllDeliveries() {
        List<Delivery> deliveries = null;
        try (SqlSession sqlSession = getSqlSession()) {
            DeliveryMapper mapper = sqlSession.getMapper(DeliveryMapper.class);
            deliveries = mapper.getAllDeliveries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deliveries == null ? new ArrayList<>() : deliveries;
    }

    @Override
    public Delivery getDelivery(String deliveryId) {
        Delivery delivery = null;
        try (SqlSession sqlSession = getSqlSession()) {
            DeliveryMapper mapper = sqlSession.getMapper(DeliveryMapper.class);
            delivery = mapper.getDelivery(deliveryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return delivery;
    }

    @Override
    public int getDeliveryNum() {
        int num = 0;
        try (SqlSession sqlSession = getSqlSession()) {
            DeliveryMapper mapper = sqlSession.getMapper(DeliveryMapper.class);
            num = mapper.getDeliveryNum();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    @Override
    public boolean addDelivery(Delivery delivery) {
        boolean success = false;
        try (SqlSession sqlSession = getSqlSession()) {
            DeliveryMapper mapper = sqlSession.getMapper(DeliveryMapper.class);
            mapper.insertDelivery(delivery);
            sqlSession.commit();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
}
