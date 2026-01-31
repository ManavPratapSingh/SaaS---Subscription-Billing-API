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
public class PlanResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private Integer durationDays;
}
