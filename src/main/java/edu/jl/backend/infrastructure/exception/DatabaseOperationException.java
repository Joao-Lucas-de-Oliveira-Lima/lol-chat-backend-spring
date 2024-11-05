package edu.jl.backend.infrastructure.exception;

public class DatabaseOperationException extends RuntimeException{
    public DatabaseOperationException(String message) {
        super(message);
    }
}
