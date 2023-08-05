package com.genug.wpob.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Builder
    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[JwtAuthenticationFilter] doFilterInternal(..) --- called");
        try {
            String token = parseBearerToken(request);
            log.info("[JwtAuthenticationFilter] doFilterInternal(..) --- token={}", token);
            if (token != null && !token.equalsIgnoreCase("null")) {
                Long userId = Long.parseLong(tokenProvider.validateAndGetSubject(token));
                request.setAttribute("userId", userId);
                log.info("[JwtAuthenticationFilter] doFilterInternal(..) --- userId={}", userId);
                var authentication = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
        } catch (Exception e) {
            log.error("[JwtAuthenticationFilter] doFilterInternal(..) ex={}", e.toString());
        }
        filterChain.doFilter(request, response);
    }

    // 요청 Header 에서 jwt 분리
    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authentication");
        log.info("[JwtAuthenticationFilter] parseBearerToken(..) --- bearerToken={}", bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
