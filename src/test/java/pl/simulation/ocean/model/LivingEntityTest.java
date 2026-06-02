package pl.simulation.ocean.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy abstrakcyjnej klasy {@link pl.simulation.ocean.model.LivingEntity}.
 * Sprawdzają stałe wspólne dla wszystkich żywych organizmów (energia, koszt ruchu)
 * oraz próg śmierci — organizm żyje tylko przy energii ściśle większej od zera.
 */
class LivingEntityTest {

    /** Weryfikuje, że stałe klasy odpowiadają zasadom symulacji (energia startowa 100, koszt ruchu 5, max 5 ruchów/turę). */
    @Test
    void sharedConstantsMatchSimulationRules() {
        assertEquals(100, LivingEntity.INITIAL_ENERGY);
        assertEquals(5, LivingEntity.MOVE_ENERGY_COST);
        assertEquals(5, LivingEntity.MAX_MOVES_PER_TURN);
    }

    /** Organizm z energią 1 jest żywy; po utracie ostatniej jednostki energii umiera. */
    @Test
    void isAliveUsesStrictlyPositiveEnergy() {
        Fish fish = new Fish("Rybka1", 0, 0);
        fish.loseEnergy(99);
        assertTrue(fish.isAlive());

        fish.loseEnergy(1);
        assertFalse(fish.isAlive());
    }
}
