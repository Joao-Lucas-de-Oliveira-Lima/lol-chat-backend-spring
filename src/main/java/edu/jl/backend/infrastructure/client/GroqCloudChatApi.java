package edu.jl.backend.infrastructure.client;

import edu.jl.backend.infrastructure.exception.FeignClientCommunicationException;
import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "groqCloudChatApi", url = "${chat-service.url}", configuration = GroqCloudChatApi.Configuration.class)
public interface GroqCloudChatApi extends GenerativeAiChatService {
    Logger logger = LoggerFactory.getLogger(GroqCloudChatApi.class.getName());

    @PostMapping("/v1/chat/completions")
    ChatApiResponse performRequestForChatCompletion(ChatApiRequest request);

    @Override
    default String generateContent(String objective, String context) throws FeignClientCommunicationException {
        try {
            String model = "llama3-8b-8192";
            List<Message> messages = List.of(
                    new Message("system", objective),
                    new Message("user", context)
            );
            ChatApiRequest request = new ChatApiRequest(model, messages);
            return performRequestForChatCompletion(request).choices.getFirst().message.content;
        } catch (Exception exception) {
            logger.error("Error occurred: { " + exception.getMessage() + " }", exception);
            throw new FeignClientCommunicationException("Failed to communicate with the Chat Completion service.");
        }
    }

    record ChatApiRequest(String model, List<Message> messages) {
    }

    record Message(String role, String content) {
    }

    record ChatApiResponse(List<Choice> choices) {
    }

    record Choice(Message message) {
    }

    class Configuration {
        @Bean
        public RequestInterceptor apiKeyRequestInterceptor(
                @Value("${chat-service.key}") String apiKey) {
            return RequestTemplate -> RequestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
        }
    }
}
