package com.project.SaaS.subscription_billing_api.repository;

import com.project.SaaS.subscription_billing_api.entity.Payment;
import com.project.SaaS.subscription_billing_api.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findBySubscriptionUserId(Long userId);

    List<Payment> findBySubscriptionUserIdAndStatus(Long userId, PaymentStatus status);
}
