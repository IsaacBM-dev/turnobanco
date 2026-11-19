package com.senati.turnobanco.exception;

import com.senati.turnobanco.dto.ErrorDetailDTO;
import com.senati.turnobanco.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja errores de @Valid (Caso 03 del PDF)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetailDTO> detalles = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorDetailDTO(err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponseDTO response = ErrorResponseDTO.of(
                "VALIDATION_ERROR",
                "Los datos enviados no son válidos.",
                detalles
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Maneja errores de validación de negocio (código duplicado, trámite inexistente, etc.)
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessValidation(BusinessValidationException ex) {
        List<ErrorDetailDTO> detalles = List.of(new ErrorDetailDTO(ex.getCampo(), ex.getMessage()));
        ErrorResponseDTO response = ErrorResponseDTO.of(
                "VALIDATION_ERROR",
                "Los datos enviados no son válidos.",
                detalles
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Maneja búsquedas o eliminaciones de recursos que no existen
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex) {
        List<ErrorDetailDTO> detalles = List.of(new ErrorDetailDTO(ex.getCampo(), ex.getMessage()));
        ErrorResponseDTO response = ErrorResponseDTO.of(
                "RESOURCE_NOT_FOUND",
                "No se encontró el recurso solicitado.",
                detalles
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

