package edu.jl.backend.presentation.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jl.backend.infra.exception.DatabaseOperationException;
import edu.jl.backend.infra.repository.ChampionRepository;
import edu.jl.backend.presentation.DTO.ChampionResponseDTO;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ChampionController}
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChampionControllerIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17");

    private final MockMvc mockMvc;
    private final ChampionMapper championMapper;
    private final ObjectMapper objectMapper;
    private final ChampionRepository championRepository;

    @Autowired
    public ChampionControllerIT(MockMvc mockMvc, ChampionMapper championMapper, ObjectMapper objectMapper, ChampionRepository championRepository) {
        this.mockMvc = mockMvc;
        this.championMapper = championMapper;
        this.objectMapper = objectMapper;
        this.championRepository = championRepository;
    }

    @Test
    @DisplayName("Verify that the connection to PostgreSQL was established correctly")
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Should return all champions as DTOs successfully with HTTP status 200")
    void shouldReturnAllChampionsAsDTOsWithStatus200() throws Exception {
        ResultActions requestResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/champions"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        byte[] jsonResponseContentInBytes =
                requestResult.andReturn().getResponse().getContentAsByteArray();
        String jsonResponseContentAsString =
                new String(jsonResponseContentInBytes, StandardCharsets.UTF_8);

        List<ChampionResponseDTO> actualChampionDTOs =
                objectMapper.readValue(jsonResponseContentAsString, new TypeReference<>() {});

        List<ChampionResponseDTO> expectedChampionDTOs =
                championRepository.findAll().stream().map(championMapper::mapToResponseDTO).toList();

        assertThat(expectedChampionDTOs).isEqualTo(actualChampionDTOs);
    }

    @Test
    @DisplayName("Should throw DatabaseOperationException when the database " +
            "connection is lost during fetching champions")
    void shouldThrowDatabaseOperationExceptionWhenConnectionIsLost() throws Exception{
        postgres.stop();

        ResultActions requestResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/champions"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(500));

        byte[] jsonResponseContentInBytes =
                requestResult.andReturn().getResponse().getContentAsByteArray();
        String jsonResponseContentAsString =
                new String(jsonResponseContentInBytes, StandardCharsets.UTF_8);

        DatabaseOperationException exceptionObtained =
                objectMapper.readValue(jsonResponseContentAsString, new TypeReference<>() {});

        assertThat(exceptionObtained).isInstanceOf(DatabaseOperationException.class);
    }
}