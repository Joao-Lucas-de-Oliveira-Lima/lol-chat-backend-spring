package edu.jl.backend.presentation.DTO;

import java.util.Date;

public record ExceptionDTO(
        Date timestamp,
        String details,
        String message
) {
}
