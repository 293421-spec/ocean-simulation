package pl.simulation.ocean.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy bazowej klasy {@link pl.simulation.ocean.model.Entity}.
 * Weryfikują zachowania wspólne dla wszystkich organizmów: format {@code toString}
 * oraz dostęp do pozycji przez mechanizm dziedziczenia.
 */
class EntityTest {

    /** Sprawdza, że toString planktonu zawiera nazwę klasy i współrzędne. */
    @Test
    void planktonEntityToStringUsesClassNameAndPosition() {
        Plankton plankton = new Plankton(2, 5);
        assertTrue(plankton.toString().startsWith("Plankton@"));
        assertTrue(plankton.toString().contains("(2, 5)"));
    }

    /** Potwierdza, że rybka dziedziczy getter i setter pozycji po Entity. */
    @Test
    void fishInheritsEntityPositionAccess() {
        Fish fish = new Fish("Rybka1", 1, 2);
        fish.setPosition(fish.getPosition());
        assertEquals(1, fish.getPosition().getX());
        assertEquals(2, fish.getPosition().getY());
    }
}
