package com.project.SaaS.subscription_billing_api.controller;

import com.project.SaaS.subscription_billing_api.annotation.RateLimit;
import com.project.SaaS.subscription_billing_api.dto.CreateSubscriptionRequest;
import com.project.SaaS.subscription_billing_api.dto.SubscriptionResponse;
import com.project.SaaS.subscription_billing_api.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscriptions", description = "Subscription management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @RateLimit
    @Operation(summary = "Create a new subscription", description = "Creates a subscription for the user with the specified plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription created successfully", content = @Content(schema = @Schema(implementation = SubscriptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token", content = @Content),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded", content = @Content)
    })
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Parameter(description = "Subscription creation details", required = true) @Valid @RequestBody CreateSubscriptionRequest request) {

        SubscriptionResponse response = subscriptionService.createSubscription(
                request.getUserId(),
                request.getPlanId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
