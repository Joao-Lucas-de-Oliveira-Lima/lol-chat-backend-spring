package edu.jl.backend.application.gateway;

import edu.jl.backend.domain.entity.Champion;

import java.util.List;

public interface ChampionGateway {
    List<Champion> listChampions() throws Exception;

    Champion findChampionById(Long id) throws Exception;

    String askAChampion(String objective, String context) throws Exception;
}
