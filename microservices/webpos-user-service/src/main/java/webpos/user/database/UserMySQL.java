package webpos.user.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Repository;
import webpos.user.mapper.UserMapper;
import webpos.user.pojo.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Repository
public class UserMySQL implements UserDB {
    private SqlSessionFactory sqlSessionFactory;

    public UserMySQL() {
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
    public User getUser(String userId) {
        User order = null;
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            order = mapper.getUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public boolean addUser(User user) {
        boolean success = false;
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.insertUser(user);
            sqlSession.commit();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
}
