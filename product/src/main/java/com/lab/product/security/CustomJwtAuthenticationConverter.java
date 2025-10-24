package com.lab.product.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Custom JWT Authentication Converter for NEXA Bank
 * Extracts roles from JWT token and converts them to Spring Security authorities
 * 
 * JWT Structure:
 * {
 *   "sub": "admin@nexabank.com",
 *   "jti": "bbfcd4f6-47cf-4935-b0d5-3fe12533178f",
 *   "userId": "cc11f7d0-dfaa-42dd-b5a6-098ef87e9b60",
 *   "userType": "ADMIN",
 *   "roles": "ADMIN_FULL_ACCESS,ADMIN_VIEW,ADMIN_REPORTS,ADMIN_SYSTEM_CONFIG,ADMIN_USER_MANAGEMENT",
 *   "iat": 1761121018,
 *   "exp": 1761121048
 * }
 */
@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Extract userType and add as authority (e.g., ROLE_ADMIN)
        String userType = jwt.getClaimAsString("userType");
        if (userType != null && !userType.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userType));
        }
        
        // Extract roles claim (comma-separated string)
        String rolesString = jwt.getClaimAsString("roles");
        if (rolesString != null && !rolesString.isEmpty()) {
            String[] roles = rolesString.split(",");
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.trim()));
            }
        }
        
        return authorities;
    }
}
