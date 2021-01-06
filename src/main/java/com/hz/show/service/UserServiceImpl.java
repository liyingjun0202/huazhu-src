package com.hz.show.service;

import com.alibaba.fastjson.JSONObject;
import com.hz.show.dao.UserDao;
import com.hz.show.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liyj
 * @Description
 * @createTime 2021/1/6 10:22
 **/
@Service
public class UserServiceImpl implements  IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getUserList(Map<String, Object> map) {
        return userDao.getUserList(map);
    }

    @Override
    public User getUser(User user) {
        return userDao.getUser(user);
    }

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUsers(String[] ids) {
        userDao.deleteUsers(ids);
    }
}
