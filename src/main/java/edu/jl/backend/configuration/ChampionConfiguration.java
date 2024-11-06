package edu.jl.backend.configuration;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.application.usercase.AskAChampionInteractor;
import edu.jl.backend.application.usercase.ListChampionsInteractor;
import edu.jl.backend.infrastructure.client.GenerativeAiChatService;
import edu.jl.backend.infrastructure.gateway.ChampionGatewayImpl;
import edu.jl.backend.infrastructure.repository.ChampionRepository;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChampionConfiguration {

    @Bean
    public ChampionMapper createChampionMapper(){
        return new ChampionMapper();
    }

    @Bean
    public ChampionGateway createChampionGateway(
            ChampionRepository championRepository,
            ChampionMapper championMapper,
            GenerativeAiChatService generativeAiChatService)
    {
        return new ChampionGatewayImpl(championRepository, championMapper, generativeAiChatService);
    }

    @Bean
    public ListChampionsInteractor createChampionInteractor(ChampionGateway championGateway){
        return new ListChampionsInteractor(championGateway);
    }

    @Bean
    public AskAChampionInteractor createAskAChampionInteractor(
            ChampionGateway championGateway){
        return new AskAChampionInteractor(championGateway);
    }
}
