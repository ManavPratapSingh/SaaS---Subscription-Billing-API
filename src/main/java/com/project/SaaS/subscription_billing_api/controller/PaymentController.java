package com.project.SaaS.subscription_billing_api.controller;

import com.project.SaaS.subscription_billing_api.annotation.RateLimit;
import com.project.SaaS.subscription_billing_api.dto.InvoiceResponse;
import com.project.SaaS.subscription_billing_api.dto.PaymentResponse;
import com.project.SaaS.subscription_billing_api.security.JwtUtil;
import com.project.SaaS.subscription_billing_api.service.PaymentService;
import com.project.SaaS.subscription_billing_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/process", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @RateLimit
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestParam("subscriptionId") Long subscriptionId,
            @RequestPart(value = "receipt", required = false) org.springframework.web.multipart.MultipartFile receiptFile) {

        PaymentResponse response = paymentService.processMockPayment(subscriptionId, receiptFile);

        if (response.getStatus().toString().equals("SUCCESS")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
        }
    }

    @GetMapping("/my-invoices")
    public ResponseEntity<List<InvoiceResponse>> getMyInvoices(HttpServletRequest request) {
        // Extract user from JWT token
        String token = extractTokenFromRequest(request);
        String username = jwtUtil.extractUsername(token);
        Long userId = userService.getUserByUsername(username).getId();

        List<InvoiceResponse> invoices = paymentService.getMyInvoices(userId);
        return ResponseEntity.ok(invoices);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("No JWT token found in request");
    }
}
