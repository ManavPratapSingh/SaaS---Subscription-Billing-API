package com.project.SaaS.subscription_billing_api.dto;

import com.project.SaaS.subscription_billing_api.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Authentication response containing JWT token and user details")
public class AuthResponse {

    @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Token type (always Bearer)", example = "Bearer")
    @Builder.Default
    private String type = "Bearer";

    @Schema(description = "Username of the authenticated user", example = "johndoe")
    private String username;

    @Schema(description = "Email address of the authenticated user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Role of the authenticated user", example = "USER")
    private Role role;
}
