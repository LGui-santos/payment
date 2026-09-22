package com.ead.payment.configs.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class AuthenticationJwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtStr = getTokenHeader(request);
            if (jwtStr != null && jwtProvider.validateJwt(jwtStr)) {
                String userId = jwtProvider.getSubjectJwt(jwtStr);
                String roles = jwtProvider.getClaimNameJwt(jwtStr, "roles");
                UserDetails userDetails = UserDetailsImpl.build(UUID.fromString(userId), roles);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.error("Cannot set User Authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenHeader(HttpServletRequest request) {
        String requestHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(requestHeader) && requestHeader.startsWith("Bearer ")) {
            return requestHeader.substring(7);
        }
        return null;
    }
}
