package br.edu.fiec.MapeamentoDeSaude.shared.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        // Loga o erro completo no seu ficheiro de log
        log.error("Uma exceção não tratada ocorreu: {}", ex.getMessage(), ex);

        // Retorna uma resposta genérica e segura para o cliente
        return new ResponseEntity<>(
                Map.of("erro", "Ocorreu um erro inesperado no servidor."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}