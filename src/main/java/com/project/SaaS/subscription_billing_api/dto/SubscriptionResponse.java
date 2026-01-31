package com.project.SaaS.subscription_billing_api.dto;

import com.project.SaaS.subscription_billing_api.entity.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponse {

    private Long id;

    private Long userId;

    private Long planId;

    private String planName;

    private SubscriptionStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
