package com.hz.show.dao;

import com.alibaba.fastjson.JSONObject;
import com.hz.show.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author liyj
 * @Description
 * @createTime 2020/12/30 11:30
 **/
@Mapper
public interface UserDao {

    public List<User> getUserList(Map<String,Object> map);

    public User getUser(User user);

    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUsers(String[] ids);

}
