package com.project.SaaS.subscription_billing_api.dto;

import com.project.SaaS.subscription_billing_api.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payment response object")
public class PaymentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Unique identifier of the payment", example = "1")
    private Long id;

    @Schema(description = "ID of the subscription this payment is for", example = "1")
    private Long subscriptionId;

    @Schema(description = "Amount paid", example = "29.99")
    private BigDecimal amount;

    @Schema(description = "Status of the payment", example = "SUCCESS")
    private PaymentStatus status;

    @Schema(description = "Date and time of the payment", example = "2026-01-15T14:30:00")
    private LocalDateTime paymentDate;

    @Schema(description = "Unique transaction ID for the payment", example = "TXN_20260115_143000_ABC123")
    private String transactionId;

    @Schema(description = "Additional message about the payment", example = "Payment processed successfully")
    private String message;
}
