package edu.jl.backend.domain.exception;

public class InvalidQuestionException extends RuntimeException{
    public InvalidQuestionException(String message) {
        super(message);
    }
}
