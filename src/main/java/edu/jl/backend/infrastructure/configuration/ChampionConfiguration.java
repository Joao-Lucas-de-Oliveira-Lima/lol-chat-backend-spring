package edu.jl.backend.infrastructure.configuration;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.application.usercase.AskAChampionIteractor;
import edu.jl.backend.application.usercase.ListChampionsIteractor;
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
    public ListChampionsIteractor createChampionIteractor(ChampionGateway championGateway){
        return new ListChampionsIteractor(championGateway);
    }

    @Bean
    public AskAChampionIteractor createAskAChampionIteractor(
            ChampionGateway championGateway){
        return new AskAChampionIteractor(championGateway);
    }
}
