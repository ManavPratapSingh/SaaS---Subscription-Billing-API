package com.project.SaaS.subscription_billing_api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    /**
     * Number of requests allowed per time period.
     * Default: use configured value from application.yaml
     */
    int limit() default -1;

    /**
     * Time period in seconds.
     * Default: use configured value from application.yaml
     */
    int period() default -1;
}
