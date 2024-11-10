package edu.jl.backend.application.gateway;

import edu.jl.backend.domain.entity.ChampionEntity;

import java.util.List;

public interface ChampionRepositoryGateway {
    List<ChampionEntity> listChampions() throws Exception;

    ChampionEntity findChampionById(Long id) throws Exception;
}
