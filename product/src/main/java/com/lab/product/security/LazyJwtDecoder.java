package com.lab.product.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * Lazy-loading JWT Decoder that only fetches public key when first token is validated.
 * This prevents startup failures if Auth Service is temporarily unavailable.
 */
public class LazyJwtDecoder implements JwtDecoder {

    private final String jwkSetUri;
    private volatile JwtDecoder delegate;
    private final Object lock = new Object();

    public LazyJwtDecoder(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        // Double-checked locking for lazy initialization
        if (delegate == null) {
            synchronized (lock) {
                if (delegate == null) {
                    // Fetch public key from Auth Service only when first token arrives
                    delegate = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
                }
            }
        }
        return delegate.decode(token);
    }
}
