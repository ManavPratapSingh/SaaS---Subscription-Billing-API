package com.project.SaaS.subscription_billing_api.dto;

import com.project.SaaS.subscription_billing_api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    @Builder.Default
    private String type = "Bearer";

    private String username;

    private String email;

    private Role role;
}
