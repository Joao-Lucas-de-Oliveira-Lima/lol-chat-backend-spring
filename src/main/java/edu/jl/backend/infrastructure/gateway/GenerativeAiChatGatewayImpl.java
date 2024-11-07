package edu.jl.backend.infrastructure.gateway;

import edu.jl.backend.application.gateway.GenerativeAiChatGateway;
import edu.jl.backend.infrastructure.client.GenerativeAiChatService;

public class GenerativeAiChatGatewayImpl implements GenerativeAiChatGateway {
    private final GenerativeAiChatService generativeAiChatService;

    public GenerativeAiChatGatewayImpl(GenerativeAiChatService generativeAiChatService) {
        this.generativeAiChatService = generativeAiChatService;
    }

    @Override
    public String askAChampion(String objective, String context) throws Exception {
        return this.generativeAiChatService.generateContent(objective, context);
    }
}
