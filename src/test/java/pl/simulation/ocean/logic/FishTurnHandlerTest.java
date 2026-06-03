package pl.simulation.ocean.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.*;
import pl.simulation.ocean.testutil.SystemOutSilencer;
import pl.simulation.ocean.util.Position;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy klasy {@link pl.simulation.ocean.logic.FishTurnHandler}.
 * Weryfikują kompletną logikę tury rybki: pomijanie martwych jednostek,
 * priorytet ucieczki przed rekinem, zjadanie planktonu po wejściu na tę samą komórkę,
 * zużycie energii na ruch oraz śmierć z wyczerpania w trakcie tury.
 */
class FishTurnHandlerTest {

    private Ocean ocean;
    private FishTurnHandler handler;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
        handler = new FishTurnHandler(new Random(1));
    }

    /** Martwa rybka nie wykonuje żadnego ruchu i nie traci energii. */
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

    /** Rybka wykrywająca rekina w zasięgu 5 pól odpycha się od niego — odległość po turze jest większa. */
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

    /** Rybka znajdująca się na tej samej komórce co plankton zjada go (plankton staje się martwy). */
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

    /** W jednej turze rybka wykonuje dokładnie {@code MAX_MOVES_PER_TURN} ruchów, zużywając 5×5 = 25 energii. */
    @Test
    void fishConsumesEnergyForEachMoveUpToMaxPerTurn() {
        Fish fish = new Fish("Rybka1", 10, 10);
        ocean.addFish(fish);

        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(fish, ocean);
        }

        assertEquals(100 - LivingEntity.MOVE_ENERGY_COST * LivingEntity.MAX_MOVES_PER_TURN, fish.getEnergy());
    }

    /** Rybka z minimalną energią (20 = 4 ruchy × 5) ginie w połowie tury; zużywa energię tylko do śmierci. */
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

    /** Gdy rekin i plankton są w zasięgu, rybka ucieka od rekina zamiast zbliżać się do planktonu. */
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
