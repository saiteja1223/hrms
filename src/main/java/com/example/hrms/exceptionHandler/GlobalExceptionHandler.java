package com.example.hrms.exceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request,
            Authentication auth) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "ACCESS_DENIED");
        body.put("message", getCustomMessage(request.getRequestURI(), auth));
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("timestamp", LocalDateTime.now().toString());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    private String getCustomMessage(String path, Authentication auth) {
        // Example logic to return specific messages based on path
        if (path.contains("/getRoles/by-org")) {
            return "You don't have the required permissions (e.g., VIEW_SALARY or VIEW_ROLES) to view roles.";
        } else if (path.contains("/salary/")) {
            return "You are not authorized to view salary details.";
        }
        return "You don't have permission to perform this action.";
    }
}