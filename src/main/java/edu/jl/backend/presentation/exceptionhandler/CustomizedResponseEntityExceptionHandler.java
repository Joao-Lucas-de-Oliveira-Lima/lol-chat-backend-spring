package edu.jl.backend.presentation.exceptionhandler;

import edu.jl.backend.domain.exception.ChampionNotFoundException;
import edu.jl.backend.domain.exception.InvalidQuestionException;
import edu.jl.backend.infrastructure.exception.DatabaseOperationException;
import edu.jl.backend.infrastructure.exception.FeignClientCommunicationException;
import edu.jl.backend.presentation.DTO.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.InvalidClassException;
import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGlobalExceptions(
            WebRequest webRequest,
            Exception exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, webRequest, exception);
    }

    @ExceptionHandler(ChampionNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleChampionNotFoundExceptions(
            WebRequest webRequest,
            ChampionNotFoundException exception) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, webRequest, exception);
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ExceptionDTO> handleDatabaseOperationExceptions(
            WebRequest webRequest,
            DatabaseOperationException exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, webRequest, exception);
    }

    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidQuestionExceptions(
            WebRequest webRequest,
            InvalidQuestionException invalidQuestionException) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, webRequest, invalidQuestionException);
    }

    @ExceptionHandler(FeignClientCommunicationException.class)
    public ResponseEntity<ExceptionDTO> handleFeignClientCommunicationException(
            WebRequest webRequest,
            FeignClientCommunicationException feignClientCommunicationException) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, webRequest, feignClientCommunicationException);
    }

    private ResponseEntity<ExceptionDTO> buildResponseEntity(
            HttpStatus httpStatus,
            WebRequest webRequest,
            Exception exception) {
        return ResponseEntity
                .status(httpStatus)
                .body(new ExceptionDTO(
                        new Date(),
                        webRequest.getDescription(false),
                        exception.getMessage())
                );
    }
}
