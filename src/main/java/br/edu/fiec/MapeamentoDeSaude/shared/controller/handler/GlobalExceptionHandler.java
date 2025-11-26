package br.edu.fiec.MapeamentoDeSaude.shared.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError; // Importante
import org.springframework.web.bind.MethodArgumentNotValidException; // Importante
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 1. Trata erros de validação (Ex: Senha fraca, CPF nulo, Email inválido)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Log para você ver no console
        log.warn("Erro de validação: {}", errors);

        // Retorna o primeiro erro encontrado como mensagem principal ou a lista
        String mainError = errors.values().stream().findFirst().orElse("Erro de validação");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", mainError, "errors", errors));
    }

    // 2. Trata erros de Regra de Negócio (Ex: Email já cadastrado - RuntimeException)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeExceptions(RuntimeException ex) {
        log.error("Erro de execução: {}", ex.getMessage());

        // Retorna a mensagem exata que você lançou no Service (ex: "Email já cadastrado")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }

    // 3. Trata qualquer outro erro não previsto (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Erro não tratado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Ocorreu um erro interno no servidor. Contate o suporte."));
    }
}