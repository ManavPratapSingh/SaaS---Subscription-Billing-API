package com.project.SaaS.subscription_billing_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Subscription plan response object")
public class PlanResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Unique identifier of the plan", example = "1")
    private Long id;

    @Schema(description = "Name of the subscription plan", example = "Premium Plan")
    private String name;

    @Schema(description = "Price of the plan", example = "29.99")
    private BigDecimal price;

    @Schema(description = "Duration of the plan in days", example = "30")
    private Integer durationDays;
}
