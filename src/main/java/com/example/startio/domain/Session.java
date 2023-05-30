package com.example.startio.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Session {

    private String id;
    private Long insertTime;
    private String userId;
    private String buyerId;
    private Float bid;
    private Boolean win;
    private Long impressionDuration;
    private Long clickTime;
}
