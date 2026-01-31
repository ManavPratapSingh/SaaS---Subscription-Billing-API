package com.project.SaaS.subscription_billing_api.service;

import com.project.SaaS.subscription_billing_api.dto.InvoiceResponse;
import com.project.SaaS.subscription_billing_api.dto.PaymentResponse;
import com.project.SaaS.subscription_billing_api.entity.*;
import com.project.SaaS.subscription_billing_api.repository.PaymentRepository;
import com.project.SaaS.subscription_billing_api.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final Random random = new Random();

    /**
     * Process mock payment for a subscription.
     * Simulates success (70%) or failure (30%).
     */
    public PaymentResponse processMockPayment(Long subscriptionId) {
        // Fetch subscription
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));

        // Check if already active
        if (subscription.getStatus() == SubscriptionStatus.ACTIVE) {
            throw new RuntimeException("Subscription is already active");
        }

        // Generate unique transaction ID
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Simulate payment success/failure (70% success, 30% failure)
        boolean isSuccess = random.nextInt(100) < 70;

        PaymentStatus paymentStatus = isSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        // Create payment record
        Payment payment = Payment.builder()
                .subscription(subscription)
                .amount(subscription.getPlan().getPrice())
                .status(paymentStatus)
                .transactionId(transactionId)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        String message;

        if (isSuccess) {
            // Update subscription status to ACTIVE
            subscription.setStatus(SubscriptionStatus.ACTIVE);

            // Calculate end_date based on plan duration
            LocalDateTime startDate = subscription.getStartDate();
            LocalDateTime endDate = startDate.plusDays(subscription.getPlan().getDurationDays());
            subscription.setEndDate(endDate);

            subscriptionRepository.save(subscription);

            message = "Payment processed successfully. Subscription is now ACTIVE.";
            log.info("Payment successful for subscription: {} with transaction: {}", subscriptionId, transactionId);
        } else {
            message = "Payment failed. Please try again.";
            log.warn("Payment failed for subscription: {} with transaction: {}", subscriptionId, transactionId);
        }

        return PaymentResponse.builder()
                .id(savedPayment.getId())
                .subscriptionId(subscription.getId())
                .amount(savedPayment.getAmount())
                .status(savedPayment.getStatus())
                .paymentDate(savedPayment.getPaymentDate())
                .transactionId(savedPayment.getTransactionId())
                .message(message)
                .build();
    }

    /**
     * Get all invoices for a user.
     */
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getMyInvoices(Long userId) {
        List<Payment> payments = paymentRepository.findBySubscriptionUserId(userId);

        return payments.stream()
                .map(this::generateInvoice)
                .collect(Collectors.toList());
    }

    /**
     * Generate invoice from payment.
     */
    private InvoiceResponse generateInvoice(Payment payment) {
        Subscription subscription = payment.getSubscription();
        User user = subscription.getUser();
        Plan plan = subscription.getPlan();

        String invoiceNumber = "INV-" + payment.getId() + "-" +
                payment.getPaymentDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Generate invoice text
        String invoiceText = generateInvoiceText(
                invoiceNumber,
                user.getId(),
                user.getUsername(),
                payment.getAmount(),
                payment.getPaymentDate(),
                plan.getName(),
                payment.getStatus());

        return InvoiceResponse.builder()
                .invoiceNumber(invoiceNumber)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .amount(payment.getAmount())
                .date(payment.getPaymentDate())
                .planName(plan.getName())
                .status(payment.getStatus())
                .invoiceText(invoiceText)
                .build();
    }

    /**
     * Generate invoice text string.
     */
    private String generateInvoiceText(String invoiceNumber, Long userId, String username,
            java.math.BigDecimal amount, LocalDateTime date,
            String planName, PaymentStatus status) {
        StringBuilder invoice = new StringBuilder();
        invoice.append("========================================\n");
        invoice.append("           PAYMENT INVOICE\n");
        invoice.append("========================================\n\n");
        invoice.append("Invoice Number: ").append(invoiceNumber).append("\n");
        invoice.append("Date: ").append(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        invoice.append("Status: ").append(status).append("\n\n");
        invoice.append("----------------------------------------\n");
        invoice.append("Customer Information:\n");
        invoice.append("----------------------------------------\n");
        invoice.append("User ID: ").append(userId).append("\n");
        invoice.append("Username: ").append(username).append("\n\n");
        invoice.append("----------------------------------------\n");
        invoice.append("Payment Details:\n");
        invoice.append("----------------------------------------\n");
        invoice.append("Plan: ").append(planName).append("\n");
        invoice.append("Amount: $").append(amount).append("\n\n");
        invoice.append("========================================\n");

        if (status == PaymentStatus.SUCCESS) {
            invoice.append("Thank you for your payment!\n");
        } else {
            invoice.append("Payment was not successful.\n");
        }

        invoice.append("========================================\n");

        return invoice.toString();
    }
}
