package edu.jl.backend.application.usercase;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.domain.entity.Champion;

public class AskAChampionInteractor {
    private final ChampionGateway championGateway;

    public AskAChampionInteractor(ChampionGateway championGateway) {
        this.championGateway = championGateway;
    }

    public String askAChampion(Long championId, String question) throws Exception {
        Champion championObtained = championGateway.findChampionById(championId);
        String context = """
                Question: %s
                Champion name: %s
                Title: %s
                Lore (history): %s
                """.formatted(question, championObtained.getName(), championObtained.getTitle(),
                championObtained.getLore());
        String objective = """
                Act as an assistant with the ability to act like champions from the game League of Legends.
                Answer questions by embodying the personality and style of a given champion.
                Avoiding generating excessively long responses.
                Here is the question and other context information:
                """;
        return championGateway.askAChampion(objective, context);
    }
}
