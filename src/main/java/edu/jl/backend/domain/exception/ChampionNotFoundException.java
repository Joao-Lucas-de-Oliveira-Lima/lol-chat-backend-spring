package edu.jl.backend.domain.exception;

public class ChampionNotFoundException extends RuntimeException{
    public ChampionNotFoundException(String message) {
        super(message);
    }
}
