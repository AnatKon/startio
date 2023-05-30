package com.example.startio.dao.impl;

import com.example.startio.dao.ifc.SessionDao;
import com.example.startio.domain.FileType;
import com.example.startio.domain.Session;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class BaseSessionDao  extends MySqlDao implements SessionDao {

    private static final String INSERT_SESSION = "INSERT INTO Session(id, insertTime, userId, buyerId, bid, win) values(?,?,?,?,?,?)";
    private static final String SELECT_SESSIONS = "SELECT * FROM Session WHERE id in (?)";
    private static final String UPDATE_SESSION_REQUESTS = "UPDATE Session SET insertTime = ?, userId = ?, buyerId = ?, bid = ?, win = ? WHERE id=?";
    private static final String UPDATE_SESSION_IMPRESSIONS = "UPDATE Session SET impressionDuration WHERE id=?";
    private static final String UPDATE_SESSION_CLICKS = "UPDATE Session SET clickTime = ? WHERE id=?";
    private static final String COUNT_REQUESTS = "SELECT COUNT(*) FROM Session WHERE bid <> NULL AND userId=?";
    private static final String COUNT_IMMPRESSIONS = "SELECT COUNT(*) FROM Session WHERE impression<>NULL AND id=?";
    private static final String COUNT_CLICKS = "SELECT COUNT(*) FROM Session WHERE clickTime<>NULL AND id=?";
    private static final String SELECT_AVERAGE_WIN_BIDS = "SELECT AVG(Bid) FROM Session WHERE win = true AND id=?";

    private void createSessions(List<Session> sessionList) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(INSERT_SESSION, Statement.NO_GENERATED_KEYS);
                for (Session session : sessionList) {
                    ps.setString(1, session.getId());
                    ps.setLong(2, session.getInsertTime());
                    ps.setString(3, session.getUserId());
                    ps.setString(4, session.getBuyerId());
                    ps.setFloat(5, session.getBid());
                    ps.setBoolean(6, session.getWin());
                    ps.addBatch();
                }
                return ps;
            }
        });
    }

    private void updateSessions(FileType fileType, List<Session> sessionList) {
        switch (fileType) {
            case requests -> updateSessionRequests(sessionList);
            case impressions -> updateSessionImpressions(sessionList);
            case clicks -> updateSessionClicks(sessionList);
        }
    }

    private void updateSessionRequests(List<Session> sessionList) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(UPDATE_SESSION_REQUESTS, Statement.NO_GENERATED_KEYS);
                for (Session session : sessionList) {
                    ps.setLong(1, session.getInsertTime());
                    ps.setString(3, session.getUserId());
                    ps.setString(4, session.getBuyerId());
                    ps.setFloat(5, session.getBid());
                    ps.setBoolean(6, session.getWin());
                    ps.addBatch();
                }
                return ps;
            }
        });
    }

    private void updateSessionImpressions(List<Session> sessionList) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(UPDATE_SESSION_IMPRESSIONS, Statement.NO_GENERATED_KEYS);
                for (Session session : sessionList) {
                    ps.setLong(1, session.getImpressionDuration());
                    ps.addBatch();
                }
                return ps;
            }
        });
    }

    private void updateSessionClicks(List<Session> sessionList) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(UPDATE_SESSION_CLICKS, Statement.NO_GENERATED_KEYS);
                for (Session session : sessionList) {
                    ps.setLong(1, session.getClickTime());
                    ps.addBatch();
                }
                return ps;
            }
        });
    }

    private List<Session> getExistingSessions(List<Session> sessionList) {
        return jdbcTemplate.queryForList(SELECT_SESSIONS + '(' + sessionList.toString() + ')', Session.class);
    }

    public void insertOrUpdateSessions(FileType fileType, List<Session> sessionList) {
        List<Session> existingSessions = getExistingSessions(sessionList);
        sessionList.removeAll(existingSessions);

        createSessions(sessionList);
        updateSessions(fileType, existingSessions);
    }

    public Long countRequests(String userId){
        return jdbcTemplate.queryForObject(COUNT_REQUESTS, Long.class, userId);
    }

    public Long countImpressions(String userId){
        return jdbcTemplate.queryForObject(COUNT_IMMPRESSIONS, Long.class, userId);
    }

    public Long countClicks(String userId){
        return jdbcTemplate.queryForObject(COUNT_CLICKS, Long.class, userId);
    }

    public Float getAverageWinBids(String userId){
        return jdbcTemplate.queryForObject(SELECT_AVERAGE_WIN_BIDS, Float.class, userId);
    }
}
