package edu.jl.backend.application.usercase;

import edu.jl.backend.domain.exception.ChampionNotFoundException;
import edu.jl.backend.infrastructure.client.GenerativeAiChatService;
import edu.jl.backend.infrastructure.exception.FeignClientCommunicationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integration tests for {@link AskAChampionInteractor}
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class AskAChampionInteractorIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17.0");

    @Autowired
    private AskAChampionInteractor askAChampionInteractor;
    @MockBean
    private GenerativeAiChatService generativeAiChatService;

    private static String sampleChampionAnswer;
    private static String sampleQuestion;
    private static long sampleChampionId;


    @BeforeAll
    static void setupForAllTests() {
        sampleChampionAnswer = """
                I am Aatrox, the Darkin Blade, once a noble defender of Shurima,
                now a harbinger of destruction. My purpose? To reclaim my once-glorious form,
                to ravage the lands and bring this world to its knees. I was imprisoned,
                betrayed by those I fought for, but I have returned. Mortals will suffer,
                and the very essence of Runeterra will tremble under my blade, until I am whole
                again.
                """;
        sampleQuestion = "Who is Aatrox and what is his origin in Runeterra?";
        sampleChampionId = 1L;
    }

    @Test
    @DisplayName("Verify that the connection to PostgreSQL was established correctly")
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Should successfully generate a response from a champion when a question is asked")
    void shouldGenerateChampionAnswerSuccessfully() throws Exception {
        when(generativeAiChatService.generateContent(any(String.class), any(String.class)))
                .thenReturn(sampleChampionAnswer);
        String result = askAChampionInteractor.askAChampion(sampleChampionId, sampleQuestion);
        assertThat(result).isEqualTo(sampleChampionAnswer);
        verify(generativeAiChatService, times(1))
                .generateContent(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Should throw ChampionNotFoundException when invalid champion ID is provided")
    void shouldThrowChampionNotFoundExceptionWhenInvalidChampionIdIsProvided() {
        Long invalidId = -1L;
        assertThatThrownBy(() -> askAChampionInteractor.askAChampion(invalidId, sampleQuestion))
                .isInstanceOf(ChampionNotFoundException.class)
                .hasMessage("ChampionEntity with id " + invalidId + " was not found!");
        verifyNoInteractions(generativeAiChatService);
    }

    @Test
    @DisplayName("Should throw FeignClientCommunicationException when chat service fails")
    void shouldThrowFeignClientCommunicationExceptionWhenChatServiceFails() throws Exception {
        String exceptionMessage = "Failed to communicate with the Chat Completion service.";
        when(generativeAiChatService.generateContent(any(String.class), any(String.class)))
                .thenThrow(new FeignClientCommunicationException(exceptionMessage));
        assertThatThrownBy(() -> askAChampionInteractor.askAChampion(sampleChampionId, sampleQuestion))
                .isInstanceOf(FeignClientCommunicationException.class)
                .hasMessage(exceptionMessage);
        verify(generativeAiChatService, times(1))
                .generateContent(any(String.class), any(String.class));
    }
}
