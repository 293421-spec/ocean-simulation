package pl.simulation.ocean;

import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.*;
import pl.simulation.ocean.util.Position;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void fishStartsWithFullEnergy() {
        Fish fish = new Fish("Rybka1", 5, 5);
        assertEquals(100, fish.getEnergy());
        assertTrue(fish.isAlive());
    }

    @Test
    void fishDiesWhenEnergyReachesZero() {
        Fish fish = new Fish("Rybka1", 5, 5);
        fish.loseEnergy(100);
        assertFalse(fish.isAlive());
    }

    @Test
    void sharkAttackReducesFishEnergy() {
        Shark shark = new Shark("Rekin1", 5, 5);
        Fish fish   = new Fish("Rybka1", 5, 5);
        shark.attack(fish);
        assertEquals(80, fish.getEnergy());
        assertEquals(120, shark.getEnergy());
    }

    @Test
    void planktonIsRemovedAfterEating() {
        Plankton plankton = new Plankton(3, 3);
        assertTrue(plankton.isAlive());
        plankton.eat();
        assertFalse(plankton.isAlive());
    }

    @Test
    void positionEuclideanDistance() {
        Position a = new Position(0, 0);
        Position b = new Position(3, 4);
        assertEquals(5.0, a.distanceTo(b), 0.001);
    }

    @Test
    void oceanBoundsCheck() {
        Ocean ocean = new Ocean();
        assertTrue(ocean.isWithinBounds(0, 0));
        assertTrue(ocean.isWithinBounds(19, 19));
        assertFalse(ocean.isWithinBounds(20, 0));
        assertFalse(ocean.isWithinBounds(-1, 5));
    }

    @Test
    void oceanIsOverWhenAllFishDead() {
        Ocean ocean = new Ocean();
        Fish fish = new Fish("Rybka1", 1, 1);
        ocean.addFish(fish);
        assertFalse(ocean.isSimulationOver());
        fish.loseEnergy(100);
        assertTrue(ocean.isSimulationOver());
    }
}
