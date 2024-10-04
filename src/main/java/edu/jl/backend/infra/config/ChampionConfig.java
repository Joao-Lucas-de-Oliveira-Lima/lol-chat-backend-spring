package edu.jl.backend.infra.config;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.application.usercase.ListChampionsIteractor;
import edu.jl.backend.infra.gateway.ChampionGatewayImpl;
import edu.jl.backend.infra.repository.ChampionRepository;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChampionConfig {

    @Bean
    public ChampionMapper createChampionMapper(){
        return new ChampionMapper();
    }

    @Bean
    public ChampionGateway createChampionGateway(
            ChampionRepository championRepository,
            ChampionMapper championMapper)
    {
        return new ChampionGatewayImpl(championRepository, championMapper);
    }

    @Bean
    public ListChampionsIteractor createChampionIteractor(ChampionGateway championGateway){
        return new ListChampionsIteractor(championGateway);
    }
}
