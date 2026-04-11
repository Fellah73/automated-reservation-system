package com.algerietelecom.logistics.reservationmanager.config;

import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.algerietelecom.logistics.reservationmanager.service.JwtService;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    // exclude many routes from the filter (aka the guard)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals("/auth/login");
    }

    // guard jwt method
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null) {
            try {
                if (jwtService.isTokenValid(token)) {
                    String email = jwtService.extractEmail(token);
                    request.setAttribute("userEmail", email);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            email, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    writeErrorResponse(response, "expired jwt", 401);
                    return;
                }
            } catch (Exception e) {
                writeErrorResponse(response, "Auth error : " + e.getMessage(), 401);
                return;
            }
        } else {
            writeErrorResponse(response, "No auth token provided", 401);
            return;
        }

        filterChain.doFilter(request, response);
    }

    // customized error response method
    private void writeErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");

        String jsonResponse = String.format(
                "{\"success\": false, \"message\": \"%s\", \"statusCode\": %d, \"data\": null}",
                message, statusCode);

        response.getWriter().write(jsonResponse);
    }
}