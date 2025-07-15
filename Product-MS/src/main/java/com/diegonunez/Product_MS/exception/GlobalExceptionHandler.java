package com.diegonunez.Product_MS.exception;

import com.diegonunez.Product_MS.dto.jsonapi.ProductErrorResponseJsonApi;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProductErrorResponseJsonApi> entityNotFoundExceptionHandler(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ProductErrorResponseJsonApi(
                        "404",
                        "Not Found",
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProductErrorResponseJsonApi> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ProductErrorResponseJsonApi.ErrorItem> errorItems = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ProductErrorResponseJsonApi.ErrorItem(
                        "400",
                        "Bad Request",
                        err.getField() + ": " + err.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(new ProductErrorResponseJsonApi(errorItems));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ProductErrorResponseJsonApi> handleDuplicateValuesException(SQLIntegrityConstraintViolationException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ProductErrorResponseJsonApi(
                        "409",
                        "Conflict",
                        "The product already exists"
                )
        );
    }
}
