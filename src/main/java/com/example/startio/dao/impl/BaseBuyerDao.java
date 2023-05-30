package com.example.startio.dao.impl;

import com.example.startio.dao.ifc.BuyerDao;
import com.example.startio.domain.Buyer;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class BaseBuyerDao  extends MySqlDao implements BuyerDao {

    private static final String INSERT_BUYER = "INSERT INTO Buyer(name) values(?)";

    public void createBuyers(List<Buyer> buyerList) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(INSERT_BUYER, Statement.RETURN_GENERATED_KEYS);
                for (Buyer buyer : buyerList) {
                    ps.setString(1, buyer.getName());
                    ps.addBatch();
                }
                return ps;
            }
        }, holder);
    }
}
