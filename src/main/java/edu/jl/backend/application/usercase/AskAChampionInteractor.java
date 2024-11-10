package edu.jl.backend.application.usercase;

import edu.jl.backend.application.gateway.ChampionRepositoryGateway;
import edu.jl.backend.application.gateway.GenerativeAiChatGateway;
import edu.jl.backend.domain.entity.ChampionEntity;

public class AskAChampionInteractor {
    private final ChampionRepositoryGateway championRepositoryGateway;
    private final GenerativeAiChatGateway generativeAiChatGateway;

    public AskAChampionInteractor(ChampionRepositoryGateway championRepositoryGateway, GenerativeAiChatGateway generativeAiChatGateway) {
        this.championRepositoryGateway = championRepositoryGateway;
        this.generativeAiChatGateway = generativeAiChatGateway;
    }

    public String askAChampion(Long championId, String question) throws Exception {
        ChampionEntity championObtained = championRepositoryGateway.findChampionById(championId);
        String context = """
                Question: %s
                Champion name: %s
                Title: %s
                Lore (history): %s
                """.formatted(question, championObtained.getName(), championObtained.getTitle(),
                championObtained.getLore());
        String objective = """
                Act as an assistant with the ability to act like champions from the game League of Legends.
                Answer questions by embodying the personality and style of a given championEntity.
                Avoiding generating excessively long responses.
                Here is the question and other context information:
                """;
        return generativeAiChatGateway.askAChampion(objective, context);
    }
}
