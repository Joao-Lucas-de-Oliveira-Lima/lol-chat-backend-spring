package edu.jl.backend.application.usercase;

import edu.jl.backend.application.gateway.ChampionRepositoryGateway;
import edu.jl.backend.domain.entity.ChampionEntity;
import edu.jl.backend.infrastructure.exception.DatabaseOperationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ListChampionsInteractor}
 */
@ExtendWith(MockitoExtension.class)
class ListChampionsInteractorTest {
    @Mock
    private ChampionRepositoryGateway ChampionRepositoryGateway;
    @InjectMocks
    private ListChampionsInteractor listChampionsInteractor;

    private static List<ChampionEntity> sampleChampionListEntity;


    @BeforeAll
    static void setupForAllTests() {
        sampleChampionListEntity = new ArrayList<>();
        sampleChampionListEntity.add(new ChampionEntity(
                1L,
                "Aatrox",
                "the Darkin Blade",
                "Once honored defenders of Shurima against the Void, " +
                        "Aatrox and his brethren would eventually become an even greater " +
                        "threat to Runeterra, and were defeated only by cunning mortal sorcery. " +
                        "But after centuries of imprisonment, Aatrox was the first to find...",
                "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_0.jpg")
        );
        sampleChampionListEntity.add(new ChampionEntity(
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

    }

    @Test
    @DisplayName("Should return a list with all champions from the database successfully")
    void shouldReturnChampionListSuccessfully() throws Exception {
        when(ChampionRepositoryGateway.listChampions()).thenReturn(sampleChampionListEntity);
        List<ChampionEntity> result = listChampionsInteractor.listChampions();
        assertThat(result).isEqualTo(sampleChampionListEntity);
        verify(ChampionRepositoryGateway, times(1)).listChampions();
        verifyNoMoreInteractions(ChampionRepositoryGateway);
    }

    @Test
    @DisplayName("Should throw DatabaseOperationException when champion list retrieval fails")
    void shouldThrowDatabaseOperationExceptionOnFailure() throws Exception {
        when(ChampionRepositoryGateway.listChampions()).thenThrow(DatabaseOperationException.class);

        assertThatThrownBy(() -> listChampionsInteractor.listChampions())
                .isInstanceOf(DatabaseOperationException.class);
        verify(ChampionRepositoryGateway, times(1)).listChampions();
        verifyNoMoreInteractions(ChampionRepositoryGateway);
    }
}