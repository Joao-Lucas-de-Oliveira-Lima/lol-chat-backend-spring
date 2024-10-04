package edu.jl.backend.presentation.dto;
public record ChampionResponseDTO(
        Long id,
        String name,
        String title,
        String lore,
        String imageUrl
) {
}
