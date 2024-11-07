package edu.jl.backend.infrastructure.dto;

import edu.jl.backend.infrastructure.dto.ChampionDTO;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ChampionDTO}
 */
class ChampionDTOTest {

    @Test
    @DisplayName("Should verify that equals and hashCode are implemented correctly")
    void testEqualAndHashCode() {
        EqualsVerifier.forClass(ChampionDTO.class)
                .verify();
    }

}