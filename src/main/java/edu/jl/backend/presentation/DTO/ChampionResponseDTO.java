package edu.jl.backend.presentation.DTO;
public record ChampionResponseDTO(
        Long id,
        String name,
        String title,
        String lore,
        String imageUrl
) {
}
