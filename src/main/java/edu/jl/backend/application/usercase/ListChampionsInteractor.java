package edu.jl.backend.application.usercase;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.domain.entity.Champion;

import java.util.List;

public class ListChampionsInteractor {
    private final ChampionGateway championGateway;

    public ListChampionsInteractor(ChampionGateway championGateway) {
        this.championGateway = championGateway;
    }

    public List<Champion> listChampions() throws Exception {
        return this.championGateway.listChampions();
    }
}
