package com.project.SaaS.subscription_billing_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a subscription")
public class CreateSubscriptionRequest {

    @Schema(description = "ID of the user creating the subscription", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "User ID is required")
    private Long userId;

    @Schema(description = "ID of the subscription plan", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Plan ID is required")
    private Long planId;
}
