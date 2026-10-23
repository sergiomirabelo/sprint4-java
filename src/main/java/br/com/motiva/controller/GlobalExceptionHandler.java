package br.com.motiva.controller;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Tratamento global de erros (bonus @ControllerAdvice). */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Falhas de validacao dos Services -> 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> validacao(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }

    // Violacao de constraint do banco (codigo duplicado, FK ao remover, etc.) -> 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> integridade(DataIntegrityViolationException ex) {
        return ResponseEntity.status(409)
                .body(Map.of("erro", "Operação viola uma regra de integridade do banco (registro duplicado ou em uso)"));
    }
}
