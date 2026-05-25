package pl.simulation.ocean.model;

import org.junit.jupiter.api.Test;
import pl.simulation.ocean.util.Position;

import static org.junit.jupiter.api.Assertions.*;

class FishTest {

    @Test
    void exposesConstantsFromSpec() {
        assertEquals(5.0, Fish.DETECTION_RANGE);
        assertEquals(10, Fish.PLANKTON_ENERGY);
    }

    @Test
    void startsAliveWithFullEnergyAtGivenPosition() {
        Fish fish = new Fish("Rybka1", 5, 7);

        assertEquals("Rybka1", fish.getName());
        assertEquals(100, fish.getEnergy());
        assertTrue(fish.isAlive());
        assertEquals(new Position(5, 7), fish.getPosition());
    }

    @Test
    void diesWhenEnergyReachesZeroOrBelow() {
        Fish fish = new Fish("Rybka1", 0, 0);
        fish.loseEnergy(100);
        assertFalse(fish.isAlive());
        assertEquals(0, fish.getEnergy());
    }

    @Test
    void gainAndLoseEnergyAdjustValue() {
        Fish fish = new Fish("Rybka1", 1, 1);
        fish.loseEnergy(30);
        fish.gainEnergy(15);
        assertEquals(85, fish.getEnergy());
    }

    @Test
    void consumeMoveEnergyCostsFiveWhileAlive() {
        Fish fish = new Fish("Rybka1", 2, 2);
        assertTrue(fish.consumeMoveEnergy());
        assertEquals(95, fish.getEnergy());
    }

    @Test
    void consumeMoveEnergyDoesNothingWhenDead() {
        Fish fish = new Fish("Rybka1", 2, 2);
        fish.loseEnergy(100);
        assertFalse(fish.consumeMoveEnergy());
        assertEquals(0, fish.getEnergy());
    }

    @Test
    void setPositionUpdatesLocation() {
        Fish fish = new Fish("Rybka1", 0, 0);
        fish.setPosition(new Position(9, 9));
        assertEquals(new Position(9, 9), fish.getPosition());
    }

    @Test
    void toStringIncludesNameEnergyAndPosition() {
        Fish fish = new Fish("Ala", 3, 4);
        assertEquals("Ala@(3, 4)[energia=100]", fish.toString());
    }
}
