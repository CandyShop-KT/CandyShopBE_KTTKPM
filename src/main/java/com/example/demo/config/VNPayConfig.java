package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VNPayConfig {
    @Value("${vnpay.merchant-id}")
    private String merchantId;

    @Value("${vnpay.hash-secret}")
    private String hashSecret;

    @Value("${vnpay.payment-url}")
    private String paymentUrl;

    @Value("${vnpay.return-url}")
    private String returnUrl;

    public String getMerchantId() {
        return merchantId;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }
}