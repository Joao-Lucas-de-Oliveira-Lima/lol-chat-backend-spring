package edu.jl.backend.shared.mapper;

import edu.jl.backend.domain.entity.ChampionEntity;
import edu.jl.backend.infrastructure.model.ChampionModel;
import edu.jl.backend.infrastructure.dto.ChampionDTO;

public class ChampionMapper {
    public ChampionEntity mapToEntity(ChampionModel championModel) {
        return new ChampionEntity(
                championModel.getId(),
                championModel.getName(),
                championModel.getTitle(),
                championModel.getLore(),
                championModel.getImageUrl()
        );
    }

    public ChampionDTO mapToDTO(ChampionEntity championEntity) {
        return new ChampionDTO(
                championEntity.getId(),
                championEntity.getName(),
                championEntity.getTitle(),
                championEntity.getLore(),
                championEntity.getImageUrl());
    }

    public ChampionDTO mapToDTO(ChampionModel championModel) {
        return new ChampionDTO(
                championModel.getId(),
                championModel.getName(),
                championModel.getTitle(),
                championModel.getLore(),
                championModel.getImageUrl());
    }
}
