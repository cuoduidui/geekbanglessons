package com.cdd.user.web.service;

import com.cdd.user.web.domain.User;

/**
 * @author yangfengshan
 * @create 2021-03-02 10:00
 **/
public class UserServiceImp implements UserService {
    @Override
    public boolean register(User user) {

        return false;
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User queryUserById(Long id) {
        return null;
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return null;
    }
}
