package edu.jl.backend.application.usercase;

import edu.jl.backend.application.gateway.ChampionRepositoryGateway;
import edu.jl.backend.domain.entity.ChampionEntity;

import java.util.List;

public class ListChampionsInteractor {
    private final ChampionRepositoryGateway championRepositoryGateway;

    public ListChampionsInteractor(ChampionRepositoryGateway championRepositoryGateway) {
        this.championRepositoryGateway = championRepositoryGateway;
    }

    public List<ChampionEntity> listChampions() throws Exception {
        return this.championRepositoryGateway.listChampions();
    }
}
