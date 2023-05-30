package com.example.startio.service;

import com.example.startio.dao.ifc.SessionDao;
import com.example.startio.domain.Buyer;
import com.example.startio.domain.FileType;
import com.example.startio.domain.Session;
import com.example.startio.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ClicksInsertService implements AutoCloseable {

    @Autowired
    private SessionDao mSessionDao;

    private static final int CURRENT_TIME = 0;
    private static final int SESSION_ID = 1;
    private static final int CLICK_TIME = 2;

    private final int batchLimit;
    private List<User> userList;
    private List<Buyer> buyerList;
    private List<Session> sessionsList;

    public ClicksInsertService(int batchLimit) {
        this.batchLimit = batchLimit;
        this.userList = new ArrayList<>();
        this.buyerList = new ArrayList<>();
        this.sessionsList = new ArrayList<>();
    }

    public void insert(String[] request) {
        Session session = new Session(request[SESSION_ID], null,
                null, null, null,
                null, null, Long.parseLong(request[CLICK_TIME]));

        sessionsList.add(session);

        if (userList.size() >= batchLimit) {
            sendBatch();
        }
    }

    public void sendBatch() {
        mSessionDao.insertOrUpdateSessions(FileType.clicks, sessionsList);
        System.out.format("Send batch with %d records%n", userList.size());
        userList.clear();
        buyerList.clear();
        sessionsList.clear();
    }

    @Override
    public void close() {
        if (userList.size() != 0) {
            sendBatch();
        }
    }
}
