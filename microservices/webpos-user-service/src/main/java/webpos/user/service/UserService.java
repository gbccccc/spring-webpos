package webpos.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import webpos.user.database.UserDB;
import webpos.user.pojo.User;

import java.util.function.Function;

@Service
public class UserService {
    private UserDB userDB;

    @Autowired
    public void setUserDB(UserDB userDB) {
        this.userDB = userDB;
    }

    public Mono<Boolean> passwordCheck(String userId, String password) {
        return userDB.getUser(userId).map(
                user -> user.getPassword().equals(password)
        ).defaultIfEmpty(false);
    }

    public Mono<User> register(User user) {
        return userDB.addUser(user);
    }
}
