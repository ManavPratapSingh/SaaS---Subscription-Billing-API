package com.project.SaaS.subscription_billing_api.service;

import com.project.SaaS.subscription_billing_api.entity.Plan;
import com.project.SaaS.subscription_billing_api.entity.Subscription;
import com.project.SaaS.subscription_billing_api.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Send subscription activation email to user.
     * Runs asynchronously to not block payment processing.
     */
    @Async
    public void sendSubscriptionActivationEmail(User user, Subscription subscription, Plan plan) {
        log.info("Starting to send subscription activation email to: {}", user.getEmail());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("Subscription Activated Successfully! 🎉");
            helper.setText(buildActivationEmailContent(user, subscription, plan), true);

            log.info("Email message prepared, attempting to send...");
            mailSender.send(message);
            log.info("✅ Subscription activation email sent successfully to: {}", user.getEmail());

        } catch (MessagingException e) {
            log.error("❌ MessagingException - Failed to send email to: {}", user.getEmail(), e);
            log.error("Error details: {}", e.getMessage());
        } catch (Exception e) {
            log.error("❌ Unexpected error sending email to: {}", user.getEmail(), e);
            log.error("Error type: {}, Message: {}", e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Build HTML email content for subscription activation.
     */
    private String buildActivationEmailContent(User user, Subscription subscription, Plan plan) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String startDate = subscription.getStartDate().format(formatter);
        String endDate = subscription.getEndDate().format(formatter);

        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    line-height: 1.6;
                                    color: #333;
                                }
                                .container {
                                    max-width: 600px;
                                    margin: 0 auto;
                                    padding: 20px;
                                    background-color: #f9f9f9;
                                }
                                .header {
                                    background-color: #4CAF50;
                                    color: white;
                                    padding: 20px;
                                    text-align: center;
                                    border-radius: 5px 5px 0 0;
                                }
                                .content {
                                    background-color: white;
                                    padding: 30px;
                                    border-radius: 0 0 5px 5px;
                                }
                                .details {
                                    background-color: #f5f5f5;
                                    padding: 15px;
                                    margin: 20px 0;
                                    border-left: 4px solid #4CAF50;
                                }
                                .detail-row {
                                    margin: 10px 0;
                                }
                                .label {
                                    font-weight: bold;
                                    color: #555;
                                }
                                .footer {
                                    text-align: center;
                                    margin-top: 20px;
                                    color: #777;
                                    font-size: 12px;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1>🎉 Subscription Activated!</h1>
                                </div>
                                <div class="content">
                                    <p>Dear <strong>%s</strong>,</p>

                                    <p>Great news! Your subscription has been activated successfully.</p>

                                    <div class="details">
                                        <h3>Subscription Details</h3>
                                        <div class="detail-row">
                                            <span class="label">Plan:</span> %s
                                        </div>
                                        <div class="detail-row">
                                            <span class="label">Start Date:</span> %s
                                        </div>
                                        <div class="detail-row">
                                            <span class="label">End Date:</span> %s
                                        </div>
                                        <div class="detail-row">
                                            <span class="label">Amount Paid:</span> $%s
                                        </div>
                                    </div>

                                    <p>Thank you for choosing our service. If you have any questions, feel free to reach out to our support team.</p>

                                    <p>Best regards,<br><strong>SaaS Subscription Team</strong></p>
                                </div>
                                <div class="footer">
                                    <p>This is an automated email. Please do not reply to this message.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                user.getUsername(),
                plan.getName(),
                startDate,
                endDate,
                plan.getPrice());
    }
}
