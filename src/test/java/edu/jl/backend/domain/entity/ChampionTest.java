package edu.jl.backend.domain.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Champion}
 */
class ChampionTest {

    @Test
    @DisplayName("Should verify that equals and hashCode are implemented correctly")
    void testEqualsAndHashCode() {
        EqualsVerifier.forClass(Champion.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

}