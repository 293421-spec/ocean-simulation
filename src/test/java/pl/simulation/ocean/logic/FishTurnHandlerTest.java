package pl.simulation.ocean.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.*;
import pl.simulation.ocean.testutil.SystemOutSilencer;
import pl.simulation.ocean.util.Position;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FishTurnHandlerTest {

    private Ocean ocean;
    private FishTurnHandler handler;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
        handler = new FishTurnHandler(new Random(1));
    }

    @Test
    void deadFishDoesNotMoveOrSpendEnergy() {
        Fish fish = new Fish("Rybka1", 5, 5);
        fish.loseEnergy(100);
        int energyBefore = fish.getEnergy();
        Position posBefore = new Position(fish.getPosition());

        handler.executeTurn(fish, ocean);

        assertEquals(energyBefore, fish.getEnergy());
        assertEquals(posBefore, fish.getPosition());
    }

    @Test
    void fishFleesWhenSharkWithinDetectionRange() throws Exception {
        Fish fish = new Fish("Rybka1", 10, 10);
        ocean.addFish(fish);
        ocean.addShark(new Shark("Rekin1", 10, 14));

        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(fish, ocean);
        }

        assertNotEquals(new Position(10, 10), fish.getPosition());
        assertTrue(fish.getPosition().distanceTo(new Position(10, 14)) > 4.0);
    }

    @Test
    void fishEatsPlanktonWhenSharingCell() throws Exception {
        Fish fish = new Fish("Rybka1", 4, 3);
        Plankton plankton = new Plankton(4, 3);
        ocean.addFish(fish);
        ocean.addPlankton(plankton);

        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(fish, ocean);
        }

        assertFalse(plankton.isAlive());
    }

    @Test
    void fishConsumesEnergyForEachMoveUpToMaxPerTurn() {
        Fish fish = new Fish("Rybka1", 10, 10);
        ocean.addFish(fish);

        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(fish, ocean);
        }

        assertEquals(100 - LivingEntity.MOVE_ENERGY_COST * LivingEntity.MAX_MOVES_PER_TURN, fish.getEnergy());
    }

    @Test
    void fishStopsMovingWhenEnergyRunsOutMidTurn() {
        Fish fish = new Fish("Rybka1", 10, 10);
        fish.loseEnergy(80);
        ocean.addFish(fish);
        Position start = new Position(fish.getPosition());

        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(fish, ocean);
        }

        assertFalse(fish.isAlive());
        assertEquals(0, fish.getEnergy());
        assertNotEquals(start, fish.getPosition());
    }

    @Test
    void prefersFleeOverPlanktonWhenSharkInRange() throws Exception {
        Fish fish = new Fish("Rybka1", 10, 10);
        ocean.addFish(fish);
        ocean.addShark(new Shark("Rekin1", 10, 12));
        ocean.addPlankton(new Plankton(10, 11));

        Position before = new Position(fish.getPosition());
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(fish, ocean);
        }

        assertNotEquals(before, fish.getPosition());
        assertTrue(fish.getPosition().getY() < 10 || fish.getPosition().getX() != 10);
    }
}
