package com.somoa.shoppingmall.affiliate.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SomoaApiResponse {

    private int status;
    private long timestamp;
    private String message;
    private Map<String, Object> data;
}
