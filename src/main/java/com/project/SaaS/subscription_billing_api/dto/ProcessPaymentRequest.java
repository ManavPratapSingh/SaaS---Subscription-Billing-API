package com.project.SaaS.subscription_billing_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for processing a payment")
public class ProcessPaymentRequest {

    @Schema(description = "ID of the subscription to process payment for", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Subscription ID is required")
    private Long subscriptionId;
}
