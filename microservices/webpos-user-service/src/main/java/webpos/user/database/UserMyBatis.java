package webpos.user.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import webpos.user.mapper.UserMapper;
import webpos.user.pojo.User;

import java.io.IOException;
import java.io.InputStream;

@Repository("MyBatis")
@ConditionalOnProperty(value = "spring.repository.type", havingValue = "MyBatis")
public class UserMyBatis implements UserDB {
    private SqlSessionFactory sqlSessionFactory;

    public UserMyBatis() {
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
    public Mono<User> getUser(String userId) {
        return Mono.defer(
                () -> {
                    User order = null;
                    try (SqlSession sqlSession = getSqlSession()) {
                        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                        order = mapper.getUser(userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return order == null ? Mono.empty() : Mono.just(order);
                }
        );
    }

    @Override
    public Mono<User> addUser(User user) {
        return Mono.defer(
                () -> {
                    boolean success = false;
                    try (SqlSession sqlSession = getSqlSession()) {
                        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                        mapper.insertUser(user);
                        sqlSession.commit();
                        success = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return success ? Mono.just(user) : Mono.empty();
                }
        );
    }
}