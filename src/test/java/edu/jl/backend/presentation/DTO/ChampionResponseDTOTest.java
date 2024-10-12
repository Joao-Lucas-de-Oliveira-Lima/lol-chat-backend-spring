package edu.jl.backend.presentation.DTO;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ChampionResponseDTO}
 */
class ChampionResponseDTOTest {

    @Test
    @DisplayName("Should verify that equals and hashCode are implemented correctly")
    void testEqualAndHashCode() {
        EqualsVerifier.forClass(ChampionResponseDTO.class)
                .verify();
    }

}