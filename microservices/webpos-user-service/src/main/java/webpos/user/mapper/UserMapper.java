package webpos.user.mapper;

import webpos.user.pojo.User;

public interface UserMapper {
    User getUser(String userName);

    void insertUser(User user);
}
