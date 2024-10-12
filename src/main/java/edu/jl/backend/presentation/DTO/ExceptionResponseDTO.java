package edu.jl.backend.presentation.DTO;

import java.util.Date;

public record ExceptionResponseDTO(
        Date timestamp,
        String details,
        String message
) {
}
