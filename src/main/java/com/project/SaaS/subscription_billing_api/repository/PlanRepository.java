package com.project.SaaS.subscription_billing_api.repository;

import com.project.SaaS.subscription_billing_api.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
}
