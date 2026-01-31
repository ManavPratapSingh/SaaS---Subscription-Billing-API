package com.project.SaaS.subscription_billing_api.scheduler;

import com.project.SaaS.subscription_billing_api.entity.Subscription;
import com.project.SaaS.subscription_billing_api.entity.SubscriptionStatus;
import com.project.SaaS.subscription_billing_api.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionScheduler {

    private final SubscriptionRepository subscriptionRepository;

    /**
     * Nightly cron job to expire subscriptions.
     * Runs every day at midnight (00:00:00).
     * Cron format: "second minute hour day month weekday"
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expireSubscriptions() {
        log.info("Starting nightly subscription expiry check...");

        LocalDateTime now = LocalDateTime.now();

        // Find all subscriptions (we'll filter by status in code)
        List<Subscription> allSubscriptions = subscriptionRepository.findAll();

        int expiredCount = 0;

        for (Subscription subscription : allSubscriptions) {
            // Check if subscription is ACTIVE and end_date is before now
            if (subscription.getStatus() == SubscriptionStatus.ACTIVE
                    && subscription.getEndDate().isBefore(now)) {

                subscription.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(subscription);
                expiredCount++;

                log.debug("Expired subscription ID: {} for user ID: {}",
                        subscription.getId(),
                        subscription.getUser().getId());
            }
        }

        log.info("Subscription expiry check complete. {} subscription(s) expired.", expiredCount);
    }
}
