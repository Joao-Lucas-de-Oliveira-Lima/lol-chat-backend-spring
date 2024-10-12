package edu.jl.backend.shared.mapper;

import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.infra.model.ChampionModel;
import edu.jl.backend.presentation.DTO.ChampionResponseDTO;

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

    public ChampionResponseDTO mapToResponseDTO(Champion championDomainObj) {
        return new ChampionResponseDTO(
                championDomainObj.getId(),
                championDomainObj.getName(),
                championDomainObj.getTitle(),
                championDomainObj.getLore(),
                championDomainObj.getImageUrl());
    }

    public ChampionResponseDTO mapToResponseDTO(ChampionModel championModel) {
        return new ChampionResponseDTO(
                championModel.getId(),
                championModel.getName(),
                championModel.getTitle(),
                championModel.getLore(),
                championModel.getImageUrl());
    }
}
