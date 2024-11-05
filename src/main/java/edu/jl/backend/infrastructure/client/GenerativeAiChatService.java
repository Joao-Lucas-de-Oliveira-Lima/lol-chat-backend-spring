package edu.jl.backend.infrastructure.client;

public interface GenerativeAiChatService {
    String generateContent(String objective, String context) throws Exception;
}
