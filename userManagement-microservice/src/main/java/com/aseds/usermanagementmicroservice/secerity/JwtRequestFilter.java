package com.aseds.usermanagementmicroservice.secerity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String INTERNAL_SECRET_HEADER = "X-INTERNAL-SECRET";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ROLES_CLAIM = "role";
    private static final String UNAUTHORIZED_MESSAGE = "Invalid JWT token";
    private static final String INTERNAL_SERVICE_USERNAME = "internal-service";
    private final Key secretKey;
    private final String internalSecretKey;

    public JwtRequestFilter(
            @Value("${jwt.secret}") String secretKeyString,
            @Value("${jwt.internal}") String internalSecretKey) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT secret key format", e);
        }

        this.internalSecretKey = internalSecretKey;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {


        if (authenticateInternalService(request)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            extractJwtFromRequest(request)
                    .flatMap(this::validateAndParseClaims)
                    .map(claims -> {
                        authenticateUser(claims);
                        return true;
                    });

        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean authenticateInternalService(HttpServletRequest request) {
        String internalSecret = request.getHeader(INTERNAL_SECRET_HEADER);

        if (internalSecret != null && internalSecret.equals(internalSecretKey)) {
            List<GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("USER"));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            INTERNAL_SERVICE_USERNAME,
                            null,
                            authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }

        return false;
    }

    private Optional<String> extractJwtFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()));
    }

    private Optional<Claims> validateAndParseClaims(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();


        return Optional.of(claims);
    }

    private void authenticateUser(Claims claims) {
        String username = claims.getSubject();
        if (username == null) {
            username = (String) claims.get("username");
            if (username == null) {
                Object userId = claims.get("id");
                username = userId != null ? userId.toString() : "unknown";
            }
        }

        List<GrantedAuthority> authorities = extractAuthorities(claims);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        authentication.setDetails(claims);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private List<GrantedAuthority> extractAuthorities(Claims claims) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        try {
            Object roleObj = claims.get(ROLES_CLAIM);
            if (roleObj != null) {
                if (roleObj instanceof String roleStr) {
                    authorities.add(new SimpleGrantedAuthority(roleStr));
                } else if (roleObj instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> roles = (List<String>) roleObj;

                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                }
            } else {
                authorities.add(new SimpleGrantedAuthority("USER"));
            }
        } catch (Exception e) {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }

        return authorities;
    }
}