package edu.jl.backend.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI configureOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .version("1.0.0")
                        .title("Chatting with League of Legends Champions - REST API")
                        .description("This REST API allows users to engage in interactive " +
                                "conversations with League of Legends champions, " +
                                "powered by a large language model (LLM). " +
                                "By simulating natural dialogue, the API gives users the " +
                                "experience of chatting with their favorite champions, " +
                                "offering responses based on their unique personalities, " +
                                "lore, and in-game behavior.")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                );
    }
}
