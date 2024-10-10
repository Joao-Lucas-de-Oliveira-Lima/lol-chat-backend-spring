package edu.jl.backend.presentation.dto;

import java.util.Date;

public record ExceptionResponseDTO(
        Date timestamp,
        String details,
        String message
) {
}
