package com.project.SaaS.subscription_billing_api.controller;

import com.project.SaaS.subscription_billing_api.annotation.RateLimit;
import com.project.SaaS.subscription_billing_api.dto.InvoiceResponse;
import com.project.SaaS.subscription_billing_api.dto.PaymentResponse;
import com.project.SaaS.subscription_billing_api.security.JwtUtil;
import com.project.SaaS.subscription_billing_api.service.PaymentService;
import com.project.SaaS.subscription_billing_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing and invoice management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/process", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @RateLimit
    @Operation(summary = "Process a payment", description = "Processes a mock payment for a subscription with optional receipt upload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully", content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "402", description = "Payment required - payment processing failed", content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token", content = @Content),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded", content = @Content)
    })
    public ResponseEntity<PaymentResponse> processPayment(
            @Parameter(description = "ID of the subscription to process payment for", required = true) @RequestParam("subscriptionId") Long subscriptionId,
            @Parameter(description = "Payment receipt file (optional, max 5MB)") @RequestPart(value = "receipt", required = false) org.springframework.web.multipart.MultipartFile receiptFile) {

        PaymentResponse response = paymentService.processMockPayment(subscriptionId, receiptFile);

        if (response.getStatus().toString().equals("SUCCESS")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
        }
    }

    @GetMapping("/my-invoices")
    @Operation(summary = "Get my invoices", description = "Retrieves all invoices for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully", content = @Content(schema = @Schema(implementation = InvoiceResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token", content = @Content)
    })
    public ResponseEntity<List<InvoiceResponse>> getMyInvoices(HttpServletRequest request) {
        // Extract user from JWT token
        String token = extractTokenFromRequest(request);
        String username = jwtUtil.extractUsername(token);
        Long userId = userService.getUserByUsername(username).getId();

        List<InvoiceResponse> invoices = paymentService.getMyInvoices(userId);
        return ResponseEntity.ok(invoices);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("No JWT token found in request");
    }
}
