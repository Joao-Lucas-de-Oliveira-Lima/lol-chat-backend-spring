package edu.jl.backend.domain.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ChampionEntity}
 */
class ChampionEntityTest {

    @Test
    @DisplayName("Should verify that equals and hashCode are implemented correctly")
    void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ChampionEntity.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

}