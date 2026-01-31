package com.project.SaaS.subscription_billing_api.service;

import com.project.SaaS.subscription_billing_api.dto.AdminStatsResponse;
import com.project.SaaS.subscription_billing_api.entity.Subscription;
import com.project.SaaS.subscription_billing_api.entity.SubscriptionStatus;
import com.project.SaaS.subscription_billing_api.repository.SubscriptionRepository;
import com.project.SaaS.subscription_billing_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    /**
     * Get admin statistics including MRR and user counts.
     */
    public AdminStatsResponse getAdminStats() {
        log.info("Calculating admin statistics...");

        // Get total users count
        Long totalUsers = userRepository.count();

        // Get all subscriptions
        List<Subscription> allSubscriptions = subscriptionRepository.findAll();
        Long totalSubscriptions = (long) allSubscriptions.size();

        // Filter active subscriptions and calculate MRR
        List<Subscription> activeSubscriptions = allSubscriptions.stream()
                .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE)
                .toList();

        Long activeCount = (long) activeSubscriptions.size();

        // Calculate MRR (sum of all active subscription plan prices)
        BigDecimal mrr = activeSubscriptions.stream()
                .map(sub -> sub.getPlan().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Count pending subscriptions
        Long pendingCount = allSubscriptions.stream()
                .filter(sub -> sub.getStatus() == SubscriptionStatus.PENDING)
                .count();

        // Count expired subscriptions
        Long expiredCount = allSubscriptions.stream()
                .filter(sub -> sub.getStatus() == SubscriptionStatus.EXPIRED)
                .count();

        log.info("Admin stats calculated - MRR: {}, Total Users: {}, Active Subs: {}",
                mrr, totalUsers, activeCount);

        return AdminStatsResponse.builder()
                .mrr(mrr)
                .totalUsers(totalUsers)
                .activeSubscriptions(activeCount)
                .totalSubscriptions(totalSubscriptions)
                .pendingSubscriptions(pendingCount)
                .expiredSubscriptions(expiredCount)
                .build();
    }
}
