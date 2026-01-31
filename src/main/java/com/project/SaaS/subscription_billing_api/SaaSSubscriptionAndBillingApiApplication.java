package com.project.SaaS.subscription_billing_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SaaSSubscriptionAndBillingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaaSSubscriptionAndBillingApiApplication.class, args);
	}

}
