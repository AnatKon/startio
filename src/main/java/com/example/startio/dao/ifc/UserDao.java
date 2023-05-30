package com.example.startio.dao.ifc;

import com.example.startio.domain.User;

import java.util.List;

public interface UserDao {

    void createUsers(List<User> userList);
}
