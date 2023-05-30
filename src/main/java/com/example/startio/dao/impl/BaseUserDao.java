package com.example.startio.dao.impl;

import com.example.startio.dao.ifc.UserDao;
import com.example.startio.domain.User;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class BaseUserDao extends MySqlDao implements UserDao {

    private static final String INSERT_USER = "INSERT INTO User(id) values (?)";

    @Override
    public void createUsers(List<User> userList) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(INSERT_USER, Statement.NO_GENERATED_KEYS);
                for (User user : userList) {
                    ps.setString(1, user.getId());
                    ps.addBatch();
                }
                return ps;
            }
        });
    }
}
