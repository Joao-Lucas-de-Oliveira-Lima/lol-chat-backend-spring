package edu.jl.backend.presentation.exceptionhandler;

import edu.jl.backend.domain.exception.ChampionNotFoundException;
import edu.jl.backend.infra.exception.DatabaseOperationException;
import edu.jl.backend.presentation.dto.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleGlobalExceptions(
            WebRequest webRequest,
            Exception exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, webRequest, exception);
    }

    @ExceptionHandler(ChampionNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleChampionNotFoundException(
            WebRequest webRequest,
            ChampionNotFoundException exception) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, webRequest, exception);
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDatabaseOperationExceptions(
            WebRequest webRequest,
            DatabaseOperationException exception){
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, webRequest, exception);
    }

    private ResponseEntity<ExceptionResponseDTO> buildResponseEntity(
            HttpStatus httpStatus,
            WebRequest webRequest,
            Exception exception) {
        return ResponseEntity
                .status(httpStatus)
                .body(new ExceptionResponseDTO(
                        new Date(),
                        webRequest.getDescription(false),
                        exception.getMessage())
                );
    }
}
