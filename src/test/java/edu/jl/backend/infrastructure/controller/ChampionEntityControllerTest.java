package edu.jl.backend.infrastructure.controller;

import edu.jl.backend.application.usercase.AskAChampionInteractor;
import edu.jl.backend.application.usercase.ListChampionsInteractor;
import edu.jl.backend.domain.entity.ChampionEntity;
import edu.jl.backend.domain.exception.ChampionNotFoundException;
import edu.jl.backend.domain.exception.InvalidQuestionException;
import edu.jl.backend.infrastructure.exception.DatabaseOperationException;
import edu.jl.backend.infrastructure.exception.FeignClientCommunicationException;
import edu.jl.backend.infrastructure.dto.AnswerFromTheChampionDTO;
import edu.jl.backend.infrastructure.dto.ChampionDTO;
import edu.jl.backend.infrastructure.dto.QuestionForAChampionDTO;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ChampionController}
 */
@ExtendWith(MockitoExtension.class)
class ChampionEntityControllerTest {
    @Mock
    private ListChampionsInteractor listChampionsInteractor;
    @Mock
    private ChampionMapper championMapper;
    @Mock
    private AskAChampionInteractor askAChampionInteractor;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private ChampionController championController;

    private static QuestionForAChampionDTO sampleQuestion;
    private static String sampleChampionAnswer;
    private static Long sampleChampionId;
    private static List<ChampionEntity> sampleChampionEntityList;
    private static List<ChampionDTO> sampleChampionDTOList;


    @BeforeAll
    static void setupForAllTests() {
        sampleChampionEntityList = new ArrayList<>();
        sampleChampionEntityList.add(new ChampionEntity(
                1L,
                "Aatrox",
                "the Darkin Blade",
                "Once honored defenders of Shurima against the Void, " +
                        "Aatrox and his brethren would eventually become an even greater " +
                        "threat to Runeterra, and were defeated only by cunning mortal sorcery. " +
                        "But after centuries of imprisonment, Aatrox was the first to find...",
                "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_0.jpg")
        );
        sampleChampionEntityList.add(new ChampionEntity(
                2L,
                "Ahri",
                "the Nine-Tailed Fox",
                "Innately connected to the magic of the spirit realm, " +
                        "Ahri is a fox-like vastaya who can manipulate her prey's emotions " +
                        "and consume their essence—receiving flashes of their " +
                        "memory and insight from each soul she consumes. Once a powerful " +
                        "yet wayward...",
                "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Ahri_0.jpg")
        );

        sampleChampionDTOList = new ArrayList<>();
        sampleChampionDTOList.add(new ChampionDTO(
                sampleChampionEntityList.get(0).getId(),
                sampleChampionEntityList.get(0).getName(),
                sampleChampionEntityList.get(0).getTitle(),
                sampleChampionEntityList.get(0).getLore(),
                sampleChampionEntityList.get(0).getImageUrl()
        ));
        sampleChampionDTOList.add(new ChampionDTO(
                sampleChampionEntityList.get(1).getId(),
                sampleChampionEntityList.get(1).getName(),
                sampleChampionEntityList.get(1).getTitle(),
                sampleChampionEntityList.get(1).getLore(),
                sampleChampionEntityList.get(1).getImageUrl()
        ));

        sampleQuestion = new QuestionForAChampionDTO("What is your purpose, Aatrox?");
        sampleChampionAnswer = """
                I am Aatrox, the Darkin Blade, once a noble defender of Shurima,
                now a harbinger of destruction. My purpose? To reclaim my once-glorious form,
                to ravage the lands and bring this world to its knees. I was imprisoned,
                betrayed by those I fought for, but I have returned. Mortals will suffer,
                and the very essence of Runeterra will tremble under my blade, until I am whole
                again.
                """;
        sampleChampionId = 1L;
    }

    @Test
    @DisplayName("Should return all champions successfully with status 200 OK")
    void shouldReturnAllChampionsSuccessfully() throws Exception {
        when(listChampionsInteractor.listChampions()).thenReturn(sampleChampionEntityList);
        when(championMapper.mapToDTO(sampleChampionEntityList.get(0)))
                .thenReturn(sampleChampionDTOList.get(0));
        when(championMapper.mapToDTO(sampleChampionEntityList.get(1)))
                .thenReturn(sampleChampionDTOList.get(1));

        ResponseEntity<List<ChampionDTO>> response = championController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sampleChampionDTOList);

        verify(listChampionsInteractor, times(1)).listChampions();
        verify(championMapper, times(1)).mapToDTO(sampleChampionEntityList.get(0));
        verify(championMapper, times(1)).mapToDTO(sampleChampionEntityList.get(1));

        verifyNoMoreInteractions(championMapper);
    }

    @Test
    @DisplayName("Should throw DatabaseOperationException when an error occurs " +
            "during database access attempting to fetch champions")
    void shouldThrowDatabaseOperationExceptionWhenDatabaseAccessFailsToFetchChampions() throws Exception {
        when(listChampionsInteractor.listChampions()).thenThrow(DatabaseOperationException.class);

        assertThatThrownBy(() -> championController.findAll())
                .isInstanceOf(DatabaseOperationException.class);

        verify(listChampionsInteractor, times(1)).listChampions();
        verifyNoInteractions(championMapper);
    }

    @Test
    @DisplayName("Should successfully generate a response from a champion when a question is asked")
    void shouldGenerateChampionAnswerSuccessfully() throws Exception {
        when(askAChampionInteractor.askAChampion(sampleChampionId, sampleQuestion.question()))
                .thenReturn(sampleChampionAnswer);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<AnswerFromTheChampionDTO> controllerResponse =
                championController.ask(sampleChampionId, sampleQuestion, bindingResult);

        assertThat(Objects.requireNonNull(controllerResponse.getBody()).answer()).isEqualTo(sampleChampionAnswer);
        assertThat(controllerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(askAChampionInteractor, times(1))
                .askAChampion(sampleChampionId, sampleQuestion.question());
        verify(bindingResult, times(1)).hasErrors();
    }

    @Test
    @DisplayName("Should throw ChampionNotFoundException when invalid champion ID is provided")
    void shouldThrowChampionNotFoundExceptionWhenInvalidChampionIdIsProvided() throws Exception {
        Long invalidId = -1L;
        when(askAChampionInteractor.askAChampion(invalidId, sampleQuestion.question()))
                .thenThrow(ChampionNotFoundException.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        assertThatThrownBy(() -> championController.ask(invalidId, sampleQuestion, bindingResult))
                .isInstanceOf(ChampionNotFoundException.class);

        verify(askAChampionInteractor, times(1))
                .askAChampion(invalidId, sampleQuestion.question());
        verify(bindingResult, times(1)).hasErrors();
    }

    @Test
    @DisplayName("Should throw InvalidQuestionException when invalid question is provided")
    void shouldThrowInvalidQuestionExceptionWhenInvalidQuestionIsProvided() {
        QuestionForAChampionDTO invalidQuestion = new QuestionForAChampionDTO(null);

        when(bindingResult.hasErrors()).thenReturn(true);

        assertThatThrownBy(() -> championController.ask(sampleChampionId, invalidQuestion, bindingResult))
                .isInstanceOf(InvalidQuestionException.class);

        verifyNoInteractions(askAChampionInteractor);
        verify(bindingResult, times(1)).hasErrors();
    }

    @Test
    @DisplayName("Should throw FeignClientCommunicationException when chat service fails")
    void shouldThrowFeignClientCommunicationExceptionWhenChatServiceFails() throws Exception {
        when(askAChampionInteractor.askAChampion(sampleChampionId, sampleQuestion.question()))
                .thenThrow(FeignClientCommunicationException.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        assertThatThrownBy(() -> championController.ask(sampleChampionId, sampleQuestion, bindingResult))
                .isInstanceOf(FeignClientCommunicationException.class);
        verify(askAChampionInteractor, times(1))
                .askAChampion(sampleChampionId, sampleQuestion.question());
        verify(bindingResult, times(1)).hasErrors();
    }

    @Test
    @DisplayName("Should throw DatabaseOperationException when an error occurs " +
            "during database access trying to generate a champion response")
    void shouldThrowDatabaseOperationExceptionWhenDatabaseAccessFailsAttemptingToGenerateChampionResponse() throws Exception {
        when(askAChampionInteractor.askAChampion(sampleChampionId, sampleQuestion.question()))
                .thenThrow(DatabaseOperationException.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        assertThatThrownBy(() -> championController.ask(sampleChampionId, sampleQuestion, bindingResult))
                .isInstanceOf(DatabaseOperationException.class);
        verify(askAChampionInteractor, times(1))
                .askAChampion(sampleChampionId, sampleQuestion.question());
        verify(bindingResult, times(1)).hasErrors();
    }

}