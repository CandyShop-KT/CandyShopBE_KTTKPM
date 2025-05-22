package com.example.demo.controller;

import com.example.demo.dto.VNPayPaymentRequest;
import com.example.demo.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Order;
import com.example.demo.model.Payment;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import java.time.LocalDateTime;
import com.example.demo.model.enums.OrderStatus;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments/vnpay")
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(
            @Valid @RequestBody VNPayPaymentRequest request,
            HttpServletRequest httpRequest) {
        
        // Set default values if not provided
        if (request.getOrderInfo() == null) {
            request.setOrderInfo("Thanh toan don hang");
        }
        if (request.getOrderType() == null) {
            request.setOrderType("other");
        }
        if (request.getLocale() == null) {
            request.setLocale("vn");
        }
        if (request.getIpAddress() == null) {
            request.setIpAddress(getClientIp(httpRequest));
        }

        String paymentUrl = vnPayService.createPaymentUrl(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/return")
    public ResponseEntity<Map<String, String>> paymentReturn(
            @RequestParam Map<String, String> allParams) {
        // Lưu thông tin thanh toán vào DB
        String orderId = allParams.get("vnp_TxnRef");
        String transactionNo = allParams.get("vnp_TransactionNo");
        String status = allParams.get("vnp_ResponseCode"); // 00 là thành công
        Double amount = 0d;
        try {
            amount = Double.valueOf(allParams.get("vnp_Amount")) / 100;
        } catch (Exception ignored) {}

        Order order = null;
        if (orderId != null) {
            order = orderRepository.findById(orderId).orElse(null);
        }
        if (order != null) {
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setPaymentMethod("VNPAY");
            payment.setPaymentStatus("00".equals(status) ? "SUCCESS" : "FAILED");
            payment.setAmount(amount);
            payment.setTransactionNo(transactionNo);
            payment.setPaymentTime(LocalDateTime.now());
            paymentRepository.save(payment);
            // Cập nhật trạng thái đơn hàng nếu thanh toán thành công
            if ("00".equals(status)) {
                order.setStatus(OrderStatus.PAYMENT_SUCCESS);
                orderRepository.save(order);
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        response.put("message", allParams.get("vnp_Message"));
        response.put("transactionId", transactionNo);
        return ResponseEntity.ok(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}