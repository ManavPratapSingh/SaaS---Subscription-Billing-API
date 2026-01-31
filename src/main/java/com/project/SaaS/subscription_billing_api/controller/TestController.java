package com.project.SaaS.subscription_billing_api.controller;

import com.project.SaaS.subscription_billing_api.entity.Plan;
import com.project.SaaS.subscription_billing_api.entity.Subscription;
import com.project.SaaS.subscription_billing_api.entity.User;
import com.project.SaaS.subscription_billing_api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<String> testEmail() {
        // Create test data
        User testUser = User.builder()
                .username("Test User")
                .email("test@example.com") // Change this to your email
                .build();

        Plan testPlan = Plan.builder()
                .name("Premium Plan")
                .price(java.math.BigDecimal.valueOf(29.99))
                .durationDays(30)
                .build();

        Subscription testSubscription = Subscription.builder()
                .user(testUser)
                .plan(testPlan)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .build();

        try {
            emailService.sendSubscriptionActivationEmail(testUser, testSubscription, testPlan);
            return ResponseEntity.ok("Email sending initiated. Check logs for status.");
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }
}
