package com.fastx.live_score.core.utils;

import com.fastx.live_score.core.exception.PlayerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Component
@Slf4j
public class GlobalExceptionHandler {

    // Handle custom PlayerNotFoundException
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<AppResponse<Object>> handlePlayerNotFound(PlayerNotFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
    }

    // Handle IllegalArgumentException or any bad input
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppResponse<Object>> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));
    }

    // Handle DataIntegrityViolationException (Foreign Key Constraints)
    // This happens when trying to delete a player who is linked to other tables
    // (e.g., TeamPlayers)
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<AppResponse<Object>> handleDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException ex,
            WebRequest request) {
        log.error("Data integrity violation: ", ex);
        String message = "Cannot delete this player because they are assigned to a team or match.";
        return buildResponse(HttpStatus.CONFLICT, message, request.getDescription(false));
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse<Object>> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
    }

    private ResponseEntity<AppResponse<Object>> buildResponse(HttpStatus status, String message, String path) {
        return new ResponseEntity<>(AppResponse.builder()
                .success(false)
                .data(null)
                .message(message)
                .build(), status);
    }
}
