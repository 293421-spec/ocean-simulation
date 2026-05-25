package pl.simulation.ocean.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.*;
import pl.simulation.ocean.testutil.SystemOutSilencer;
import pl.simulation.ocean.util.Position;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SharkTurnHandlerTest {

    private Ocean ocean;
    private SharkTurnHandler handler;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
        handler = new SharkTurnHandler(new Random(2));
    }

    @Test
    void deadSharkDoesNotAct() {
        Shark shark = new Shark("Rekin1", 5, 5);
        shark.loseEnergy(100);
        Position pos = new Position(shark.getPosition());

        handler.executeTurn(shark, ocean);

        assertEquals(pos, shark.getPosition());
        assertEquals(0, shark.getEnergy());
    }

    @Test
    void sharkAttacksFishOnSameCell() throws Exception {
        Shark shark = new Shark("Rekin1", 8, 8);
        Fish fish = new Fish("Rybka1", 8, 8);
        fish.loseEnergy(80);
        ocean.addShark(shark);
        ocean.addFish(fish);

        try (SystemOutSilencer silencer = new SystemOutSilencer()) {
            handler.executeTurn(shark, ocean);
            assertTrue(silencer.captured().contains("zaatakował"));
        }

        assertEquals(0, fish.getEnergy());
        assertFalse(fish.isAlive());
    }

    @Test
    void sharkEatsPlanktonOnSameCell() throws Exception {
        Shark shark = new Shark("Rekin1", 2, 2);
        Plankton plankton = new Plankton(2, 2);
        ocean.addShark(shark);
        ocean.addPlankton(plankton);

        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(shark, ocean);
        }

        assertFalse(plankton.isAlive());
    }

    @Test
    void sharkMovesTowardFishWithinDetectionRange() throws Exception {
        Shark shark = new Shark("Rekin1", 0, 0);
        Fish fish = new Fish("Rybka1", 5, 0);
        ocean.addShark(shark);
        ocean.addFish(fish);

        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(shark, ocean);
        }

        assertTrue(shark.getPosition().distanceTo(fish.getPosition())
                < fish.getPosition().distanceTo(new Position(0, 0)));
    }

    @Test
    void sharkConsumesMoveEnergyEachTurn() {
        Shark shark = new Shark("Rekin1", 15, 15);
        ocean.addShark(shark);

        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(shark, ocean);
        }

        assertEquals(100 - LivingEntity.MOVE_ENERGY_COST * LivingEntity.MAX_MOVES_PER_TURN, shark.getEnergy());
    }

    @Test
    void sharkPrioritizesFishOverCloserPlanktonInRange() throws Exception {
        Shark shark = new Shark("Rekin1", 10, 10);
        Fish fish = new Fish("Rybka1", 10, 16);
        Plankton plankton = new Plankton(10, 11);
        ocean.addShark(shark);
        ocean.addFish(fish);
        ocean.addPlankton(plankton);

        int startY = shark.getPosition().getY();
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(shark, ocean);
        }

        assertTrue(shark.getPosition().getY() > startY);
    }
}
