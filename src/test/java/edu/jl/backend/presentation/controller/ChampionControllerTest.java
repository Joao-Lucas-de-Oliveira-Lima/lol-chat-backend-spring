package edu.jl.backend.presentation.controller;

import edu.jl.backend.application.usercase.ListChampionsIteractor;
import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.infrastructure.exception.DatabaseOperationException;
import edu.jl.backend.presentation.DTO.ChampionResponseDTO;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ChampionController}
 */
@ExtendWith(MockitoExtension.class)
class ChampionControllerTest {

    @Mock
    private ListChampionsIteractor listChampionsIteractor;
    @Mock
    private ChampionMapper championMapper;
    @InjectMocks
    private ChampionController championController;

    private static List<Champion> sampleChampionList;
    private static List<ChampionResponseDTO> sampleChampionDTOList;


    @BeforeAll
    static void setupForAllTests() {
        sampleChampionList = new ArrayList<>();
        sampleChampionList.add(new Champion(
                1L,
                "Aatrox",
                "the Darkin Blade",
                "Once honored defenders of Shurima against the Void, " +
                        "Aatrox and his brethren would eventually become an even greater " +
                        "threat to Runeterra, and were defeated only by cunning mortal sorcery. " +
                        "But after centuries of imprisonment, Aatrox was the first to find...",
                "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_0.jpg")
        );
        sampleChampionList.add(new Champion(
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
        sampleChampionDTOList.add(new ChampionResponseDTO(
                sampleChampionList.get(0).getId(),
                sampleChampionList.get(0).getName(),
                sampleChampionList.get(0).getTitle(),
                sampleChampionList.get(0).getLore(),
                sampleChampionList.get(0).getImageUrl()
        ));
        sampleChampionDTOList.add(new ChampionResponseDTO(
                sampleChampionList.get(1).getId(),
                sampleChampionList.get(1).getName(),
                sampleChampionList.get(1).getTitle(),
                sampleChampionList.get(1).getLore(),
                sampleChampionList.get(1).getImageUrl()
        ));
    }

    @Test
    @DisplayName("Should return all champions successfully with status 200 OK")
    void shouldReturnAllChampionsSuccessfully() throws Exception{
        when(listChampionsIteractor.listChampions()).thenReturn(sampleChampionList);
        when(championMapper.mapToResponseDTO(sampleChampionList.get(0)))
                .thenReturn(sampleChampionDTOList.get(0));
        when(championMapper.mapToResponseDTO(sampleChampionList.get(1)))
                .thenReturn(sampleChampionDTOList.get(1));

        ResponseEntity<List<ChampionResponseDTO>> response = championController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sampleChampionDTOList);

        verifyNoMoreInteractions(listChampionsIteractor);
        verifyNoMoreInteractions(championMapper);
    }

    @Test
    @DisplayName("Should throw DatabaseOperationException when an error occurs during database access")
    void shouldThrowExceptionWhenDatabaseAccessFails() throws Exception{
        when(listChampionsIteractor.listChampions()).thenThrow(DatabaseOperationException.class);

        assertThatThrownBy(() -> championController.findAll())
                .isInstanceOf(DatabaseOperationException.class);

        verifyNoMoreInteractions(listChampionsIteractor);
        verifyNoInteractions(championMapper);
    }
}