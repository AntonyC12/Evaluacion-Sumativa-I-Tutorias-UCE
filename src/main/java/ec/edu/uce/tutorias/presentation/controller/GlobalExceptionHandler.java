package ec.edu.uce.tutorias.presentation.controller;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> manejarDomainException(DomainException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Error interno en el servidor: " + ex.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }
}
