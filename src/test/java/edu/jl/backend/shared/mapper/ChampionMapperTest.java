package edu.jl.backend.shared.mapper;

import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.infrastructure.model.ChampionModel;
import edu.jl.backend.presentation.DTO.ChampionDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ChampionMapper}
 */

class ChampionMapperTest {

    private static ChampionModel championModel;
    private static Champion championEntity;
    private static ChampionDTO championDTO;

    private final ChampionMapper championMapper = new ChampionMapper();

    @BeforeAll
    static void setupForAllTests() {

        championEntity = new Champion(
                1L,
                "Aatrox",
                "the Darkin Blade",
                "Once honored defenders of Shurima against the Void, " +
                        "Aatrox and his brethren would eventually become an even greater " +
                        "threat to Runeterra, and were defeated only by cunning mortal sorcery. " +
                        "But after centuries of imprisonment, Aatrox was the first to find...",
                "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_0.jpg"
        );
        championModel = new ChampionModel(
                championEntity.getId(),
                championEntity.getName(),
                championEntity.getTitle(),
                championEntity.getLore(),
                championEntity.getImageUrl()
        );
        championDTO = new ChampionDTO(
                championEntity.getId(),
                championEntity.getName(),
                championEntity.getTitle(),
                championEntity.getLore(),
                championEntity.getImageUrl()
        );
    }

    @Test
    @DisplayName("Should map ChampionModel to Champion entity successfully")
    void shouldMapChampionModelToEntity() {
        Champion mappedChampionEntity  = championMapper.mapToEntity(championModel);
        assertThat(mappedChampionEntity ).isEqualTo(championEntity);
    }

    @Test
    @DisplayName("Should map Champion entity to ChampionDTO successfully")
    void shouldMapChampionEntityToResponseDTO() {
        ChampionDTO mappedChampionDTO =
                championMapper.mapToDTO(championEntity);
        assertThat(mappedChampionDTO).isEqualTo(championDTO);
    }

    @Test
    @DisplayName("Should map ChampionModel to ChampionDTO correctly")
    void shouldMapChampionModelToResponseDTO() {
        ChampionDTO mappedChampionDTO = championMapper.mapToDTO(championModel);
        assertThat(mappedChampionDTO).isEqualTo(championDTO);
    }

}