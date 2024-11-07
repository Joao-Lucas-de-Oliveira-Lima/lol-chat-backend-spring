package edu.jl.backend.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record QuestionForAChampionDTO(
        @NotBlank
        String question
) {
}
