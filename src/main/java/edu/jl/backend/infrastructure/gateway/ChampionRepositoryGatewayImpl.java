package edu.jl.backend.infrastructure.gateway;

import edu.jl.backend.application.gateway.ChampionRepositoryGateway;
import edu.jl.backend.domain.entity.ChampionEntity;
import edu.jl.backend.domain.exception.ChampionNotFoundException;
import edu.jl.backend.infrastructure.exception.DatabaseOperationException;
import edu.jl.backend.infrastructure.model.ChampionModel;
import edu.jl.backend.infrastructure.repository.ChampionRepository;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ChampionRepositoryGatewayImpl implements ChampionRepositoryGateway {
    private static final Logger logger = LoggerFactory.getLogger(ChampionRepositoryGatewayImpl.class.getName());
    private final ChampionRepository championRepository;
    private final ChampionMapper championMapper;

    public ChampionRepositoryGatewayImpl(ChampionRepository championRepository, ChampionMapper championMapper) {
        this.championRepository = championRepository;
        this.championMapper = championMapper;
    }

    @Override
    public List<ChampionEntity> listChampions() throws DatabaseOperationException {
        try {
            List<ChampionModel> champions = championRepository.findAll();
            return champions.stream().map(championMapper::mapToEntity).toList();
        } catch (Exception exception) {
            logger.error("Error occurred: { " + exception.getMessage() + " }", exception);
            throw new DatabaseOperationException("Unexpected error retrieving champions.");
        }
    }

    @Override
    public ChampionEntity findChampionById(Long id) throws DatabaseOperationException, ChampionNotFoundException {
        try {
            ChampionModel champion = championRepository.findById(id)
                    .orElseThrow(() -> new ChampionNotFoundException("ChampionEntity with id " + id + " was not found!"));
            return championMapper.mapToEntity(champion);
        } catch (ChampionNotFoundException championNotFoundException) {
            logger.error("Error occurred: { " + championNotFoundException.getMessage() + " }", championNotFoundException);
            throw championNotFoundException;
        } catch (Exception exception) {
            logger.error("Error occurred: { " + exception.getMessage() + " }", exception);
            throw new DatabaseOperationException("Unexpected error while retrieving champion.");
        }
    }
}
