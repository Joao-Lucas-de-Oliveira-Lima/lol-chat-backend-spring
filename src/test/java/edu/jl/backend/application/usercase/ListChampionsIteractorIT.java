package edu.jl.backend.application.usercase;

import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.infra.exception.DatabaseOperationException;
import edu.jl.backend.infra.repository.ChampionRepository;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for {@link ListChampionsIteractor}
 */
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class ListChampionsIteractorIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17.0");
    private final ListChampionsIteractor listChampionsIteractor;
    private final ChampionRepository championRepository;
    private final ChampionMapper championMapper;

    @Autowired
    public ListChampionsIteractorIT(
            ListChampionsIteractor listChampionsIteractor,
            ChampionRepository championRepository,
            ChampionMapper championMapper) {
        this.listChampionsIteractor = listChampionsIteractor;
        this.championRepository = championRepository;
        this.championMapper = championMapper;
    }

    @Test
    @DisplayName("Verify that the connection to PostgreSQL was established correctly")
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Should successfully return a list of champions matching the " +
            "expected data from the repository")
    void shouldReturnListOfChampionsMatchingRepositoryData() throws Exception {
        List<Champion> expectedChampions =
                championRepository.findAll().stream().map(championMapper::mapToEntity).toList();
        List<Champion> result = listChampionsIteractor.listChampions();
        assertThat(result).hasSize(expectedChampions.size());
        assertThat(result).isEqualTo(expectedChampions);
    }

    @Test
    @DisplayName("Should throw DatabaseOperationException when database connection is lost")
    void ShouldThrowDatabaseOperationExceptionWhenDatabaseOffline() {
        postgres.stop();
        assertThatThrownBy(listChampionsIteractor::listChampions)
                .isInstanceOf(DatabaseOperationException.class);
    }
}