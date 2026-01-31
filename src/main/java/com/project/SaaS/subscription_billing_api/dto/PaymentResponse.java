package com.project.SaaS.subscription_billing_api.dto;

import com.project.SaaS.subscription_billing_api.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;

    private Long subscriptionId;

    private BigDecimal amount;

    private PaymentStatus status;

    private LocalDateTime paymentDate;

    private String transactionId;

    private String message;
}
