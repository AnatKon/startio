package com.example.startio.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserResponse {

    private Long requestsNum;
    private Long impressionsNum;
    private Long clicksNum;
    private Float AveragePriceBid;
}
