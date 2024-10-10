package edu.jl.backend.infra.exception;

public class DatabaseOperationException extends RuntimeException{
    public DatabaseOperationException(String message) {
        super(message);
    }
}
