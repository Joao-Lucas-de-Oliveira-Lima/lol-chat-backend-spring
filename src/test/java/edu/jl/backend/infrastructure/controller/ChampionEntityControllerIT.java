package edu.jl.backend.infrastructure.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jl.backend.infrastructure.client.GenerativeAiChatService;
import edu.jl.backend.infrastructure.dto.AnswerFromTheChampionDTO;
import edu.jl.backend.infrastructure.dto.ChampionDTO;
import edu.jl.backend.infrastructure.dto.ExceptionDTO;
import edu.jl.backend.infrastructure.dto.QuestionForAChampionDTO;
import edu.jl.backend.infrastructure.exception.FeignClientCommunicationException;
import edu.jl.backend.infrastructure.repository.ChampionRepository;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link ChampionController}
 */
@SpringBootTest()
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChampionEntityControllerIT {
    @Autowired
    private ChampionRepository championRepository;
    @Autowired
    private ChampionMapper championMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private GenerativeAiChatService generativeAiChatService;
    private static QuestionForAChampionDTO sampleQuestion;
    private static String sampleChampionAnswer;
    private static Long sampleChampionId;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:17");

    @BeforeAll
    static void setupForAllTests() {
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
    @DisplayName("Verify that the connection to PostgreSQL was established correctly")
    void connectionEstablished() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Should return all champions as DTOs successfully with HTTP status 200")
    void shouldReturnAllChampionsAsDTOsWithStatus200() throws Exception {
        ResultActions requestResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/champions"))
                .andDo(print())
                .andExpect(status().isOk());

        byte[] jsonResponseContentInBytes =
                requestResult.andReturn().getResponse().getContentAsByteArray();
        String jsonResponseContentAsString =
                new String(jsonResponseContentInBytes, StandardCharsets.UTF_8);

        List<ChampionDTO> actualChampionDTOs =
                objectMapper.readValue(jsonResponseContentAsString, new TypeReference<>() {
                });

        List<ChampionDTO> expectedChampionDTOs =
                championRepository.findAll().stream().map(championMapper::mapToDTO).toList();
        assertThat(expectedChampionDTOs).isEqualTo(actualChampionDTOs);
    }

    @Test
    @DisplayName("Should successfully generate a champion response when asked a question")
    void shouldGenerateChampionResponseSuccessfully() throws Exception {
        when(generativeAiChatService.generateContent(any(String.class), any(String.class)))
                .thenReturn(sampleChampionAnswer);

        ResultActions requestResult = mockMvc
                .perform(post("/champions/ask/{id}", sampleChampionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleQuestion)))
                .andDo(print())
                .andExpect(status().isOk());

        byte[] jsonResponseContentInBytes =
                requestResult.andReturn().getResponse().getContentAsByteArray();

        String jsonResponseContentAsString =
                new String(jsonResponseContentInBytes, StandardCharsets.UTF_8);

        AnswerFromTheChampionDTO responseObtainedFromTheChampion =
                objectMapper.readValue(jsonResponseContentAsString, new TypeReference<>() {
                });

        AnswerFromTheChampionDTO expectedResponseFromTheChampion =
                new AnswerFromTheChampionDTO(sampleChampionAnswer);

        assertThat(responseObtainedFromTheChampion).isEqualTo(expectedResponseFromTheChampion);
        verify(generativeAiChatService, times(1))
                .generateContent(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Should throw ChampionNotFoundException when invalid champion ID is provided")
    void shouldThrowChampionNotFoundExceptionWhenInvalidChampionIdIsProvided() throws Exception {
        Long invalidId = -1L;

        ResultActions requestResult = mockMvc
                .perform(post("/champions/ask/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleQuestion)))
                .andDo(print())
                .andExpect(status().is(404));

        byte[] jsonResponseContentInBytes =
                requestResult.andReturn().getResponse().getContentAsByteArray();

        String jsonResponseContentAsString = new String(jsonResponseContentInBytes, StandardCharsets.UTF_8);

        ExceptionDTO exceptionObtainedFromTheResponse =
                objectMapper.readValue(jsonResponseContentAsString, new TypeReference<>() {
                });

        assertThat(exceptionObtainedFromTheResponse.details()).isNotBlank();
        assertThat(exceptionObtainedFromTheResponse.message())
                .isEqualTo("ChampionEntity with id " + invalidId + " was not found!");
        assertThat(exceptionObtainedFromTheResponse.timestamp()).isNotNull();

        verifyNoInteractions(generativeAiChatService);
    }

    @Test
    @DisplayName("Should throw InvalidQuestionException when question is null")
    void shouldThrowInvalidQuestionExceptionWhenQuestionIsNull() throws Exception {
        QuestionForAChampionDTO invalidQuestion = new QuestionForAChampionDTO(null);

        String invalidQuestionAsJsonString = objectMapper.writeValueAsString(invalidQuestion);

        ResultActions requestResult = mockMvc
                .perform(post("/champions/ask/{id}", sampleChampionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidQuestionAsJsonString))
                .andDo(print())
                .andExpect(status().is(400));

        byte[] jsonResponseContentInBytes =
                requestResult.andReturn().getResponse().getContentAsByteArray();

        String jsonResponseContentAsString = new String(jsonResponseContentInBytes, StandardCharsets.UTF_8);

        ExceptionDTO exceptionObtainedFromTheResponse =
                objectMapper.readValue(jsonResponseContentAsString, new TypeReference<>() {
                });

        assertThat(exceptionObtainedFromTheResponse.details()).isNotBlank();
        assertThat(exceptionObtainedFromTheResponse.message())
                .isEqualTo("Question cannot be blank or missing!");
        assertThat(exceptionObtainedFromTheResponse.timestamp()).isNotNull();

        verifyNoInteractions(generativeAiChatService);
    }

    @Test
    @DisplayName("Should throw InvalidQuestionException when question is empty")
    void shouldThrowInvalidQuestionExceptionWhenQuestionIsEmpty() throws Exception {
        QuestionForAChampionDTO invalidQuestion = new QuestionForAChampionDTO("");

        String invalidQuestionAsJsonString = objectMapper.writeValueAsString(invalidQuestion);

        ResultActions requestResult = mockMvc
                .perform(post("/champions/ask/{id}", sampleChampionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidQuestionAsJsonString))
                .andDo(print())
                .andExpect(status().is(400));

        byte[] jsonResponseContentInBytes =
                requestResult.andReturn().getResponse().getContentAsByteArray();

        String jsonResponseContentAsString = new String(jsonResponseContentInBytes, StandardCharsets.UTF_8);

        ExceptionDTO exceptionObtainedFromTheResponse =
                objectMapper.readValue(jsonResponseContentAsString, new TypeReference<>() {
                });

        assertThat(exceptionObtainedFromTheResponse.details()).isNotBlank();
        assertThat(exceptionObtainedFromTheResponse.message())
                .isEqualTo("Question cannot be blank or missing!");
        assertThat(exceptionObtainedFromTheResponse.timestamp()).isNotNull();

        verifyNoInteractions(generativeAiChatService);
    }

    @Test
    @DisplayName("Should throw InvalidQuestionException when question contains only spaces")
    void shouldThrowInvalidQuestionExceptionWhenQuestionContainsOnlySpaces() throws Exception {
        QuestionForAChampionDTO invalidQuestion = new QuestionForAChampionDTO("     ");

        String invalidQuestionAsJsonString = objectMapper.writeValueAsString(invalidQuestion);

        ResultActions requestResult = mockMvc
                .perform(post("/champions/ask/{id}", sampleChampionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidQuestionAsJsonString))
                .andDo(print())
                .andExpect(status().is(400));

        byte[] jsonResponseContentInBytes =
                requestResult.andReturn().getResponse().getContentAsByteArray();

        String jsonResponseContentAsString = new String(jsonResponseContentInBytes, StandardCharsets.UTF_8);

        ExceptionDTO exceptionObtainedFromTheResponse =
                objectMapper.readValue(jsonResponseContentAsString, new TypeReference<>() {
                });

        assertThat(exceptionObtainedFromTheResponse.details()).isNotBlank();
        assertThat(exceptionObtainedFromTheResponse.message())
                .isEqualTo("Question cannot be blank or missing!");
        assertThat(exceptionObtainedFromTheResponse.timestamp()).isNotNull();

        verifyNoInteractions(generativeAiChatService);
    }

    @Test
    @DisplayName("Should throw FeignClientCommunicationException when chat service fails")
    void shouldThrowFeignClientCommunicationExceptionWhenChatServiceFails() throws Exception {
        when(generativeAiChatService.generateContent(any(String.class), any(String.class)))
                .thenThrow(new FeignClientCommunicationException("Failed to communicate with the Chat Completion service."));

        ResultActions resultActions = mockMvc
                .perform(post("/champions/ask/{id}", sampleChampionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleQuestion)))
                .andDo(print())
                .andExpect(status().is(500));

        byte[] jsonResponseContentInBytes =
                resultActions.andReturn().getResponse().getContentAsByteArray();

        String jsonResponseContentAsString =
                new String(jsonResponseContentInBytes, StandardCharsets.UTF_8);

        ExceptionDTO exceptionObtainedFromTheResponse =
                objectMapper.readValue(jsonResponseContentAsString, new TypeReference<>() {
                });

        assertThat(exceptionObtainedFromTheResponse.details()).isNotBlank();
        assertThat(exceptionObtainedFromTheResponse.timestamp()).isNotNull();
        assertThat(exceptionObtainedFromTheResponse.message())
                .isEqualTo("Failed to communicate with the Chat Completion service.");

        verify(generativeAiChatService, times(1)).
                generateContent(any(String.class), any(String.class));
    }
}