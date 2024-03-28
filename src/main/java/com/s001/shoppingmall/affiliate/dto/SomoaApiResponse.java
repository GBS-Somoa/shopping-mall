package com.s001.shoppingmall.affiliate.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SomoaApiResponse {

    private int status;
    private long timestamp;
    private String message;
    private Map<Integer, Object> data;
}
