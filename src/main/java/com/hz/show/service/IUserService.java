package com.hz.show.service;

import com.alibaba.fastjson.JSONObject;
import com.hz.show.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author liyj
 * @Description
 * @createTime 2021/1/6 10:21
 **/
public interface IUserService {

    public List<User> getUserList(Map<String,Object> map);

    public User getUser(User user);

    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUsers(String[] ids);

}
