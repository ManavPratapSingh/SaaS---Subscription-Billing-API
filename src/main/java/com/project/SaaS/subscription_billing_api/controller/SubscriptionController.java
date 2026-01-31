package com.project.SaaS.subscription_billing_api.controller;

import com.project.SaaS.subscription_billing_api.dto.CreateSubscriptionRequest;
import com.project.SaaS.subscription_billing_api.dto.SubscriptionResponse;
import com.project.SaaS.subscription_billing_api.service.SubscriptionService;
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
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request) {

        SubscriptionResponse response = subscriptionService.createSubscription(
                request.getUserId(),
                request.getPlanId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
