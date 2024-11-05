package edu.jl.backend.shared.mapper;

import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.infrastructure.model.ChampionModel;
import edu.jl.backend.presentation.DTO.ChampionDTO;

public class ChampionMapper {
    public Champion mapToEntity(ChampionModel championModel) {
        return new Champion(
                championModel.getId(),
                championModel.getName(),
                championModel.getTitle(),
                championModel.getLore(),
                championModel.getImageUrl()
        );
    }

    public ChampionDTO mapToDTO(Champion championDomainObj) {
        return new ChampionDTO(
                championDomainObj.getId(),
                championDomainObj.getName(),
                championDomainObj.getTitle(),
                championDomainObj.getLore(),
                championDomainObj.getImageUrl());
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
