package edu.jl.backend.presentation.DTO;

import jakarta.validation.constraints.NotBlank;

public record QuestionForAChampionDTO(
        @NotBlank
        String question
) {
}
