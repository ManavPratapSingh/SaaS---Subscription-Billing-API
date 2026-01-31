package com.project.SaaS.subscription_billing_api.repository;

import com.project.SaaS.subscription_billing_api.entity.Subscription;
import com.project.SaaS.subscription_billing_api.entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    boolean existsByUserIdAndStatus(Long userId, SubscriptionStatus status);

    Optional<Subscription> findByUserId(Long userId);
}
