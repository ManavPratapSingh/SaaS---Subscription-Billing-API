package com.project.SaaS.subscription_billing_api.controller;

import com.project.SaaS.subscription_billing_api.dto.PlanResponse;
import com.project.SaaS.subscription_billing_api.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        List<PlanResponse> plans = planService.getAllPlans();
        return ResponseEntity.ok(plans);
    }
}
