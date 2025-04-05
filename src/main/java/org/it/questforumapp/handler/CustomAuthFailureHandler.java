package org.it.questforumapp.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthFailureHandler.class);


    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        String clientIP = request.getRemoteAddr();
        String username = request.getParameter("username");


        String errorType = exception.getMessage() != null && exception.getMessage().contains("Konto zablokowane")
                ? "blocked"
                : "true";


        logger.warn("Nieudana próba logowania | Użytkownik: {} | IP: {} | Powód: {}", username, clientIP, exception.getMessage());


        response.sendRedirect("/login?error=" + errorType);
    }
}
