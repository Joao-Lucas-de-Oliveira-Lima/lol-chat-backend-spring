package edu.jl.backend.infrastructure.dto;
public record ChampionDTO(
        Long id,
        String name,
        String title,
        String lore,
        String imageUrl
) {
}
