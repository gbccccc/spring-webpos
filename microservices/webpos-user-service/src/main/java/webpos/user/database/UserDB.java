package webpos.user.database;

import webpos.user.pojo.User;

import java.util.List;

public interface UserDB {
    public User getUser(String userId);

    public boolean addUser(User user);
}
