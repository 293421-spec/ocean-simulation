package pl.simulation.ocean.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivingEntityTest {

    @Test
    void sharedConstantsMatchSimulationRules() {
        assertEquals(100, LivingEntity.INITIAL_ENERGY);
        assertEquals(5, LivingEntity.MOVE_ENERGY_COST);
        assertEquals(5, LivingEntity.MAX_MOVES_PER_TURN);
    }

    @Test
    void isAliveUsesStrictlyPositiveEnergy() {
        Fish fish = new Fish("Rybka1", 0, 0);
        fish.loseEnergy(99);
        assertTrue(fish.isAlive());

        fish.loseEnergy(1);
        assertFalse(fish.isAlive());
    }
}
