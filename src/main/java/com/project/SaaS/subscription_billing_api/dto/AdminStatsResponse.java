package com.project.SaaS.subscription_billing_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatsResponse {

    private BigDecimal mrr; // Monthly Recurring Revenue

    private Long totalUsers;

    private Long activeSubscriptions;

    private Long totalSubscriptions;

    private Long pendingSubscriptions;

    private Long expiredSubscriptions;
}
