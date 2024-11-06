package edu.jl.backend.infrastructure.exception;

public class FeignClientCommunicationException extends RuntimeException{
    public FeignClientCommunicationException(String message) {
        super(message);
    }
}
