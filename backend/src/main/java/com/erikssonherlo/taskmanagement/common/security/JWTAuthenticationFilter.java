package com.erikssonherlo.taskmanagement.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter that validates the token and sets the authentication context.
 */
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String token = extractTokenFromRequest(request);

        // If no token is provided, continue without authentication
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String username;
        try {
            username = jwtService.getUsernameFromToken(token);
        } catch (Exception e) {
            sendForbiddenResponse(response, "Access denied: Invalid JWT token.");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    sendForbiddenResponse(response, "Access denied: Invalid or expired JWT token.");
                    return;
                }
            } catch (AuthenticationException e) {
                sendForbiddenResponse(response, "Access denied: Authentication failed.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the Bearer token from the Authorization header.
     *
     * @param request HTTP request
     * @return JWT token or null if not present
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Sends a 403 Forbidden response with a custom error message.
     *
     * @param response HTTP response
     * @param message  Error message to be returned
     * @throws IOException if an input or output exception occurs
     */
    private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
    }
}
