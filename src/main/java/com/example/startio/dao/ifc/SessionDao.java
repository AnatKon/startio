package com.example.startio.dao.ifc;

import com.example.startio.domain.FileType;
import com.example.startio.domain.Session;

import java.util.List;

public interface SessionDao {

    void insertOrUpdateSessions(FileType fileType, List<Session> sessionList);
    Long countRequests(String userId);
    Long countImpressions(String userId);
    Long countClicks(String userId);
    Float getAverageWinBids(String userId);
}
