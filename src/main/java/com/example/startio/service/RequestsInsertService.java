package com.example.startio.service;

import com.example.startio.dao.ifc.BuyerDao;
import com.example.startio.dao.ifc.SessionDao;
import com.example.startio.dao.ifc.UserDao;
import com.example.startio.domain.Buyer;
import com.example.startio.domain.FileType;
import com.example.startio.domain.Session;
import com.example.startio.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestsInsertService implements AutoCloseable{

    @Autowired
    private UserDao mUserDao;

    @Autowired
    private BuyerDao mBuyerDao;

    @Autowired
    private SessionDao mSessionDao;

    private static final int INSERT_TIME = 0;
    private static final int SESSION_ID = 1;
    private static final int BUYER_NAME = 2;
    private static final int USER_ID = 3;
    private static final int BID = 4;
    private static final int WIN = 5;

    private final int batchLimit;
    private List<User> userList;
    private List<Buyer> buyerList;
    private List<Session> sessionsList;

    public RequestsInsertService(int batchLimit) {
        this.batchLimit = batchLimit;
        this.userList = new ArrayList<>();
        this.buyerList = new ArrayList<>();
        this.sessionsList = new ArrayList<>();
    }

    public void insert(String[] request) {
        User user = new User(request[USER_ID]);
        Buyer buyer = new Buyer(request[BUYER_NAME]);
        Session session = new Session(request[SESSION_ID], Long.parseLong(request[INSERT_TIME]),
                request[USER_ID], request[BUYER_NAME], Float.parseFloat(request[BID]),
                Boolean.parseBoolean(request[WIN]), null, null);

        userList.add(user);
        buyerList.add(buyer);
        sessionsList.add(session);

        if (userList.size() >= batchLimit) {
            sendBatch();
        }
    }

    public void sendBatch() {
        mUserDao.createUsers(userList);
        mBuyerDao.createBuyers(buyerList);
        mSessionDao.insertOrUpdateSessions(FileType.requests, sessionsList);
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
