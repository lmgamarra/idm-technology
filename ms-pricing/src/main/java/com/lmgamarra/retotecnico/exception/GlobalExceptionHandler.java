package com.lmgamarra.retotecnico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<String>> handleNotFound(NotFoundException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<String>> handleRuntime(RuntimeException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGeneral(Exception ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error")
        );
    }
}
