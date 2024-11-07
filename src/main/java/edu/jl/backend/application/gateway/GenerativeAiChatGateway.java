package edu.jl.backend.application.gateway;

public interface GenerativeAiChatGateway {
    String askAChampion(String objective, String context) throws Exception;
}
