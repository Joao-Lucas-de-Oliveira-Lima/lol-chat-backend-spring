package edu.jl.backend.infrastructure.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ChampionModel}
 */
class ChampionModelTest {

    @Test
    @DisplayName("Should verify that equals and hashCode are implemented correctly")
    void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ChampionModel.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}