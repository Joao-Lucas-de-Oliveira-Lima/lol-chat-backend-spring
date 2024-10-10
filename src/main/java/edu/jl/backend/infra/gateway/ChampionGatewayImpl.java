package edu.jl.backend.infra.gateway;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.infra.exception.DatabaseOperationException;
import edu.jl.backend.infra.model.ChampionModel;
import edu.jl.backend.infra.repository.ChampionRepository;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ChampionGatewayImpl implements ChampionGateway {
    private static final Logger logger = LoggerFactory.getLogger(ChampionGatewayImpl.class.getName());
    private final ChampionRepository championRepository;
    private final ChampionMapper championMapper;

    public ChampionGatewayImpl(
            ChampionRepository championRepository,
            ChampionMapper championMapper) {
        this.championRepository = championRepository;
        this.championMapper = championMapper;
    }

    @Override
    public List<Champion> listChampions() throws DatabaseOperationException {
        try {
            List<ChampionModel> champions = championRepository.findAll();
            return champions.stream().map(championMapper::mapToEntity).toList();
        } catch (Exception exception) {
            logger.error(exception.getClass().getName(), exception);
            throw new DatabaseOperationException("Unexpected error retrieving champions.");
        }
    }
}
