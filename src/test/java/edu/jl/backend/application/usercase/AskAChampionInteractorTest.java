package edu.jl.backend.application.usercase;

import edu.jl.backend.application.gateway.ChampionGateway;
import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.domain.exception.ChampionNotFoundException;
import edu.jl.backend.infrastructure.exception.DatabaseOperationException;
import edu.jl.backend.infrastructure.exception.FeignClientCommunicationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AskAChampionInteractorTest {
    @Mock
    private ChampionGateway championGateway;
    @InjectMocks
    private AskAChampionInteractor askAChampionInteractor;

    private static Long sampleChampionId;
    private static Champion sampleChampion;
    private static String sampleQuestion;
    private static String sampleContext;
    private static String sampleObjective;
    private static String sampleChampionAnswer;

    @BeforeAll
    static void setupForAllTests() {
        sampleChampionId = 1L;

        sampleChampion = new Champion(
                1L,
                "Aatrox",
                "the Darkin Blade",
                "Once honored defenders of Shurima against the Void, " +
                        "Aatrox and his brethren would eventually become an even greater " +
                        "threat to Runeterra, and were defeated only by cunning mortal sorcery. " +
                        "But after centuries of imprisonment, Aatrox was the first to find...",
                "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_0.jpg"
        );

        sampleQuestion = "Who is Aatrox and what is his origin in Runeterra?";

        sampleContext = """
                Question: %s
                Champion name: %s
                Title: %s
                Lore (history): %s
                """.formatted(sampleQuestion, sampleChampion.getName(), sampleChampion.getTitle(),
                sampleChampion.getLore());

        sampleObjective = """
                Act as an assistant with the ability to act like champions from the game League of Legends.
                Answer questions by embodying the personality and style of a given champion.
                Avoiding generating excessively long responses.
                Here is the question and other context information:
                """;
        sampleChampionAnswer = """
                I am Aatrox, the Darkin Blade, once a noble defender of Shurima, 
                now a harbinger of destruction. My purpose? To reclaim my once-glorious form, 
                to ravage the lands and bring this world to its knees. I was imprisoned, 
                betrayed by those I fought for, but I have returned. Mortals will suffer, 
                and the very essence of Runeterra will tremble under my blade, until I am whole 
                again.
                """;
    }

    @Test
    @DisplayName("Should successfully generate a response from a champion when a question is asked")
    void shouldGenerateChampionAnswerSuccessfully() throws Exception {
        when(championGateway.findChampionById(sampleChampionId)).thenReturn(sampleChampion);
        when(championGateway.askAChampion(sampleObjective, sampleContext))
                .thenReturn(sampleChampionAnswer);

        String championAnswer = askAChampionInteractor.askAChampion(sampleChampionId, sampleQuestion);

        assertThat(championAnswer).isEqualTo(sampleChampionAnswer);
        verify(championGateway, times(1)).findChampionById(sampleChampionId);
        verify(championGateway, times(1))
                .askAChampion(sampleObjective, sampleContext);
        verifyNoMoreInteractions(championGateway);
    }

    @Test
    @DisplayName("Should throw ChampionNotFoundException when invalid champion ID is provided")
    void shouldThrowChampionNotFoundExceptionWhenInvalidChampionIdIsProvided() throws Exception {
        Long invalidId = -1L;

        when(championGateway.findChampionById(invalidId))
                .thenThrow(ChampionNotFoundException.class);

        assertThatThrownBy(() -> askAChampionInteractor.askAChampion(invalidId, sampleQuestion))
                .isInstanceOf(ChampionNotFoundException.class);
        verify(championGateway, times(1))
                .findChampionById(invalidId);
        verifyNoMoreInteractions(championGateway);
    }

    @Test
    @DisplayName("Should throw FeignClientCommunicationException when chat service fails")
    void shouldThrowFeignClientCommunicationExceptionWhenChatServiceFails() throws Exception {
        when(championGateway.findChampionById(sampleChampionId)).thenReturn(sampleChampion);
        when(championGateway.askAChampion(sampleObjective, sampleContext))
                .thenThrow(FeignClientCommunicationException.class);

        assertThatThrownBy(() -> askAChampionInteractor.askAChampion(sampleChampionId, sampleQuestion))
                .isInstanceOf(FeignClientCommunicationException.class);
        verify(championGateway, times(1))
                .askAChampion(sampleObjective, sampleContext);
        verify(championGateway, times(1))
                .findChampionById(sampleChampionId);
        verifyNoMoreInteractions(championGateway);
    }

    @Test
    @DisplayName("Should throw DatabaseOperationException when an error occurs " +
            "during database access trying to generate a champion response")
    void shouldThrowDatabaseOperationExceptionWhenDatabaseAccessFailsAttemptingToGenerateChampionResponse() throws Exception {
        when(championGateway.findChampionById(sampleChampionId))
                .thenThrow(DatabaseOperationException.class);

        assertThatThrownBy(() -> askAChampionInteractor.askAChampion(sampleChampionId, sampleQuestion))
                .isInstanceOf(DatabaseOperationException.class);
        verify(championGateway, times(1))
                .findChampionById(sampleChampionId);
        verifyNoMoreInteractions(championGateway);
    }
}
