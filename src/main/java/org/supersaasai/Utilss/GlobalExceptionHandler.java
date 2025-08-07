package org.supersaasai.Utilss;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Map<String,String> map=new HashMap<>();

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UsernameNotFoundException ex) {
        map.clear();
        map.put("error",ex.getMessage());
        map.put("message","user not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(map);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        map.clear();
        map.put("error",ex.getMessage());
        map.put("message","Invalid credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(map);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error", "message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Runtime Error");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}

