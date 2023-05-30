package com.example.startio.api;

import com.example.startio.api.response.UserResponse;
import com.example.startio.dao.ifc.SessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuctionApi {

    @Autowired
    private SessionDao mSessionDao;

    @PostMapping(value = "/userStats")
    public ResponseEntity<UserResponse> getUserInfo(@RequestBody String userId){
        UserResponse userResponse = new UserResponse(mSessionDao.countRequests(userId), mSessionDao.countImpressions(userId),
                mSessionDao.countClicks(userId), mSessionDao.getAverageWinBids(userId));
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
