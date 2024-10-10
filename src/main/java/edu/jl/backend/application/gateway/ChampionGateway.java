package edu.jl.backend.application.gateway;

import edu.jl.backend.domain.entity.Champion;

import java.util.List;

public interface ChampionGateway {
    List<Champion> listChampions() throws Exception;
}
