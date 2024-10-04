package edu.jl.backend.application.usercase;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.domain.entity.Champion;

import java.util.List;

public class ListChampionsIteractor {
    private final ChampionGateway championGateway;

    public ListChampionsIteractor(ChampionGateway championGateway) {
        this.championGateway = championGateway;
    }

    public List<Champion> listChampions(){
        return this.championGateway.listChampions();
    }
}
