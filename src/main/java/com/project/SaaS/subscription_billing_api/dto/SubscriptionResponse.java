package com.project.SaaS.subscription_billing_api.dto;

import com.project.SaaS.subscription_billing_api.entity.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Subscription response object")
public class SubscriptionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Unique identifier of the subscription", example = "1")
    private Long id;

    @Schema(description = "ID of the user who owns this subscription", example = "1")
    private Long userId;

    @Schema(description = "ID of the subscription plan", example = "2")
    private Long planId;

    @Schema(description = "Name of the subscription plan", example = "Premium Plan")
    private String planName;

    @Schema(description = "Current status of the subscription", example = "ACTIVE")
    private SubscriptionStatus status;

    @Schema(description = "Start date of the subscription", example = "2026-01-01T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "End date of the subscription", example = "2026-02-01T00:00:00")
    private LocalDateTime endDate;
}
