package edu.jl.backend.infrastructure.dto;

import java.util.Date;

public record ExceptionDTO(
        Date timestamp,
        String details,
        String message
) {
}
