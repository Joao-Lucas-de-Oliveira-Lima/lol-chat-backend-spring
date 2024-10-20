package edu.jl.backend.infrastructure.gateway;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.domain.exception.ChampionNotFoundException;
import edu.jl.backend.infrastructure.client.GenerativeAiChatService;
import edu.jl.backend.infrastructure.exception.DatabaseOperationException;
import edu.jl.backend.infrastructure.model.ChampionModel;
import edu.jl.backend.infrastructure.repository.ChampionRepository;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ChampionGatewayImpl implements ChampionGateway {
    private static final Logger logger = LoggerFactory.getLogger(ChampionGatewayImpl.class.getName());
    private final ChampionRepository championRepository;
    private final ChampionMapper championMapper;
    private final GenerativeAiChatService generativeAiChatService;

    public ChampionGatewayImpl(ChampionRepository championRepository, ChampionMapper championMapper, GenerativeAiChatService generativeAiChatService) {
        this.championRepository = championRepository;
        this.championMapper = championMapper;
        this.generativeAiChatService = generativeAiChatService;
    }

    @Override
    public List<Champion> listChampions() throws DatabaseOperationException {
        try {
            List<ChampionModel> champions = championRepository.findAll();
            return champions.stream().map(championMapper::mapToEntity).toList();
        } catch (Exception exception) {
            logger.error("Error occurred: { " + exception.getMessage() + " }", exception);
            throw new DatabaseOperationException("Unexpected error retrieving champions.");
        }
    }

    @Override
    public Champion findChampionById(Long id) throws DatabaseOperationException, ChampionNotFoundException {
        try {
            ChampionModel champion = championRepository.findById(id)
                    .orElseThrow(() -> new ChampionNotFoundException("Champion with id " + id + " was not found!"));
            return championMapper.mapToEntity(champion);
        } catch (ChampionNotFoundException championNotFoundException) {
            logger.error("Error occurred: { " + championNotFoundException.getMessage() + " }", championNotFoundException);
            throw championNotFoundException;
        } catch (Exception exception) {
            logger.error("Error occurred: { " + exception.getMessage() + " }", exception);
            throw new DatabaseOperationException("Unexpected error while retrieving champion.");
        }
    }

    @Override
    public String askAChampion(String objective, String context) throws Exception {
        return this.generativeAiChatService.generateContent(objective, context);
    }
}
