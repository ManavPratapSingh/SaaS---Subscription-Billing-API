package com.project.SaaS.subscription_billing_api.exception;

public class DuplicateActiveSubscriptionException extends RuntimeException {

    public DuplicateActiveSubscriptionException(String message) {
        super(message);
    }
}
