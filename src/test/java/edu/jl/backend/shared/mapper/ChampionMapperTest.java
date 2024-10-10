package edu.jl.backend.shared.mapper;

import edu.jl.backend.domain.entity.Champion;
import edu.jl.backend.infra.model.ChampionModel;
import edu.jl.backend.presentation.dto.ChampionResponseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ChampionMapper}
 */

class ChampionMapperTest {

    private static ChampionModel championModel;
    private static Champion champion;
    private static ChampionResponseDTO championResponseDTO;

    private final ChampionMapper championMapper = new ChampionMapper();

    @BeforeAll
    static void setupForAllTests() {

        champion = new Champion(
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
                champion.getId(),
                champion.getName(),
                champion.getTitle(),
                champion.getLore(),
                champion.getImageUrl()
        );
        championResponseDTO = new ChampionResponseDTO(
                champion.getId(),
                champion.getName(),
                champion.getTitle(),
                champion.getLore(),
                champion.getImageUrl()
        );
    }

    @Test
    @DisplayName("Should map ChampionModel to Champion entity successfully")
    void shouldMapToEntity() {
        Champion result = championMapper.mapToEntity(championModel);
        assertThat(result).isEqualTo(champion);
    }

    @Test
    @DisplayName("Should map Champion entity to ChampionResponseDTO successfully")
    void shouldMapToResponseDTO() {
        ChampionResponseDTO result = championMapper.mapToResponseDTO(champion);
        assertThat(result).isEqualTo(championResponseDTO);
    }
}