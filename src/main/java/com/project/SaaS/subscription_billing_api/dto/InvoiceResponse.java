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
public class InvoiceResponse {

    private String invoiceNumber;

    private Long userId;

    private String username;

    private String email;

    private BigDecimal amount;

    private LocalDateTime date;

    private String planName;

    private PaymentStatus status;

    private String invoiceText;
}
