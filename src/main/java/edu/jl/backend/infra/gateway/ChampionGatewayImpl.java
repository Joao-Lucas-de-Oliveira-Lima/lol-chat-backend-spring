package edu.jl.backend.infra.gateway;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.infra.model.ChampionModel;
import edu.jl.backend.infra.repository.ChampionRepository;
import edu.jl.backend.shared.mapper.ChampionMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ChampionGatewayImpl implements ChampionGateway {
    private final ChampionRepository championRepository;
    private final ChampionMapper championMapper;

    public ChampionGatewayImpl(
            ChampionRepository championRepository,
            ChampionMapper championMapper) {
        this.championRepository = championRepository;
        this.championMapper = championMapper;
    }

    @Override
    public List<Champion> listChampions() {
        List<ChampionModel> champions = championRepository.findAll();
        return champions.stream().map(championMapper::mapToEntity).toList();
    }
}
