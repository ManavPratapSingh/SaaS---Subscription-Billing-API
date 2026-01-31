package com.project.SaaS.subscription_billing_api.service;

import com.project.SaaS.subscription_billing_api.dto.SubscriptionResponse;
import com.project.SaaS.subscription_billing_api.entity.Plan;
import com.project.SaaS.subscription_billing_api.entity.Subscription;
import com.project.SaaS.subscription_billing_api.entity.SubscriptionStatus;
import com.project.SaaS.subscription_billing_api.entity.User;
import com.project.SaaS.subscription_billing_api.exception.DuplicateActiveSubscriptionException;
import com.project.SaaS.subscription_billing_api.repository.SubscriptionRepository;
import com.project.SaaS.subscription_billing_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PlanService planService;

    public SubscriptionResponse createSubscription(Long userId, Long planId) {
        try {
            // Validate user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            // Validate plan exists
            Plan plan = planService.getPlanById(planId);

            // Check if user already has an active subscription
            if (subscriptionRepository.existsByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)) {
                throw new DuplicateActiveSubscriptionException(
                        "User already has an active subscription. Only one active subscription is allowed per user.");
            }

            // Calculate end date based on plan duration
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusDays(plan.getDurationDays());

            // Create new subscription with PENDING status
            Subscription subscription = Subscription.builder()
                    .user(user)
                    .plan(plan)
                    .status(SubscriptionStatus.PENDING)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            // Save subscription
            Subscription savedSubscription = subscriptionRepository.save(subscription);

            log.info("Created subscription with id: {} for user: {} with status: {}",
                    savedSubscription.getId(), userId, savedSubscription.getStatus());

            return mapToSubscriptionResponse(savedSubscription);

        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Optimistic locking failure while creating subscription for user: {}", userId, e);
            throw new RuntimeException(
                    "Unable to create subscription due to concurrent modification. Please try again.", e);
        }
    }

    private SubscriptionResponse mapToSubscriptionResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .planId(subscription.getPlan().getId())
                .planName(subscription.getPlan().getName())
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .build();
    }
}
