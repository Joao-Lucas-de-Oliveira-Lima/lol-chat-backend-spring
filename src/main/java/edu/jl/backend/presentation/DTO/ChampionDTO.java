package edu.jl.backend.presentation.DTO;
public record ChampionDTO(
        Long id,
        String name,
        String title,
        String lore,
        String imageUrl
) {
}
