package com.s001.shoppingmall.affiliate.service;

import com.s001.shoppingmall.affiliate.dto.OrderPostApiRequest;
import com.s001.shoppingmall.affiliate.dto.SomoaApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SomoaApiService {

    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${somoa-service.base-url}")
    private String serviceBaseUrl;

    @Value("${somoa-service.api.order-save}")
    private String orderSaveApi;

    @Value("${somoa-service.api.order-status-change}")
    private String orderStatusChangeApi;

    /**
     * [POST] /api/orders
     */
    public boolean callOrderSaveApi(OrderPostApiRequest apiRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderPostApiRequest> request = new HttpEntity<>(apiRequest, headers);
        log.info("url={}", serviceBaseUrl + orderSaveApi);
        SomoaApiResponse response = restTemplate.postForObject(serviceBaseUrl + orderSaveApi, request, SomoaApiResponse.class);
        return response != null && response.getStatus() == 200;
    }
}
