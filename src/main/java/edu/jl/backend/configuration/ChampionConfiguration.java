package edu.jl.backend.configuration;

import edu.jl.backend.application.gateway.ChampionRepositoryGateway;
import edu.jl.backend.application.gateway.GenerativeAiChatGateway;
import edu.jl.backend.application.usercase.AskAChampionInteractor;
import edu.jl.backend.application.usercase.ListChampionsInteractor;
import edu.jl.backend.infrastructure.client.GenerativeAiChatService;
import edu.jl.backend.infrastructure.gateway.ChampionRepositoryGatewayImpl;
import edu.jl.backend.infrastructure.gateway.GenerativeAiChatGatewayImpl;
import edu.jl.backend.infrastructure.repository.ChampionRepository;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChampionConfiguration {
    @Bean
    public ChampionMapper createChampionMapper() {
        return new ChampionMapper();
    }

    @Bean
    public ChampionRepositoryGateway createChampionRepositoryGateway(
            ChampionRepository championRepository,
            ChampionMapper championMapper) {
        return new ChampionRepositoryGatewayImpl(championRepository, championMapper);
    }

    @Bean
    public GenerativeAiChatGateway createGenerativeAiChatGateway(GenerativeAiChatService generativeAiChatService) {
        return new GenerativeAiChatGatewayImpl(generativeAiChatService);
    }

    @Bean
    public ListChampionsInteractor createListChampionsInteractor(
            ChampionRepositoryGateway championRepositoryGateway) {
        return new ListChampionsInteractor(championRepositoryGateway);
    }

    @Bean
    public AskAChampionInteractor createAskAChampionInteractor(
            ChampionRepositoryGateway championRepositoryGateway,
            GenerativeAiChatGateway generativeAiChatGateway) {
        return new AskAChampionInteractor(championRepositoryGateway, generativeAiChatGateway);
    }
}
