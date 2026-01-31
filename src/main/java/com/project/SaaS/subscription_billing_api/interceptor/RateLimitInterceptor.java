package com.project.SaaS.subscription_billing_api.interceptor;

import com.project.SaaS.subscription_billing_api.annotation.RateLimit;
import com.project.SaaS.subscription_billing_api.exception.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Value("${rate-limit.requests-per-minute:100}")
    private int defaultRequestsPerMinute;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);

        if (rateLimit == null) {
            return true; // No rate limiting for this endpoint
        }

        // Get user identifier (username or IP)
        String userId = getUserIdentifier(request);

        // Get or create bucket for user
        Bucket bucket = buckets.computeIfAbsent(userId, k -> createBucket(rateLimit));

        // Try to consume a token
        if (bucket.tryConsume(1)) {
            return true;
        } else {
            log.warn("Rate limit exceeded for user: {}", userId);
            throw new RateLimitExceededException("Rate limit exceeded. Please try again later.");
        }
    }

    private Bucket createBucket(RateLimit rateLimit) {
        int limit = rateLimit.limit() > 0 ? rateLimit.limit() : defaultRequestsPerMinute;
        int period = rateLimit.period() > 0 ? rateLimit.period() : 60;

        Bandwidth bandwidth = Bandwidth.classic(limit, Refill.intervally(limit, Duration.ofSeconds(period)));
        return Bucket.builder()
                .addLimit(bandwidth)
                .build();
    }

    private String getUserIdentifier(HttpServletRequest request) {
        // Try to get authenticated username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }

        // Fall back to IP address for unauthenticated requests
        String clientIp = request.getRemoteAddr();
        return "ip:" + clientIp;
    }
}
