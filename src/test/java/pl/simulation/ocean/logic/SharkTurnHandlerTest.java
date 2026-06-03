package pl.simulation.ocean.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.*;
import pl.simulation.ocean.testutil.SystemOutSilencer;
import pl.simulation.ocean.util.Position;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy klasy {@link pl.simulation.ocean.logic.SharkTurnHandler}.
 * Weryfikują logikę tury rekina: pomijanie martwych rekinów, atakowanie rybki
 * na tej samej komórce (z komunikatem i transferem energii), zjadanie planktonu,
 * ruch w kierunku rybki w zasięgu oraz priorytet polowania na rybkę nad planktonem.
 */
class SharkTurnHandlerTest {

    private Ocean ocean;
    private SharkTurnHandler handler;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
        handler = new SharkTurnHandler(new Random(2));
    }

    /** Martwy rekin nie porusza się i nie zmienia energii. */
    @Test
    void deadSharkDoesNotAct() {
        Shark shark = new Shark("Rekin1", 5, 5);
        shark.loseEnergy(100);
        Position pos = new Position(shark.getPosition());

        handler.executeTurn(shark, ocean);

        assertEquals(pos, shark.getPosition());
        assertEquals(0, shark.getEnergy());
    }

    /**
     * Rekin stojący na tej samej komórce co osłabiona rybka (energia 20) atakuje ją
     * — rybka ginie, a w logach pojawia się słowo "zaatakował".
     */
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

    /** Rekin na komórce z planktonem zjada go podczas tury. */
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

    /** Rekin wykrywający rybkę w zasięgu 7 pól zbliża się do niej — odległość po turze maleje. */
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

    /** Rekin zużywa dokładnie 5×5 = 25 energii na ruch w ciągu jednej tury. */
    @Test
    void sharkConsumesMoveEnergyEachTurn() {
        Shark shark = new Shark("Rekin1", 15, 15);
        ocean.addShark(shark);

        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            handler.executeTurn(shark, ocean);
        }

        assertEquals(100 - LivingEntity.MOVE_ENERGY_COST * LivingEntity.MAX_MOVES_PER_TURN, shark.getEnergy());
    }

    /** Rekin goni rybkę (dalej, ale wyżej priorytetowo) zamiast zbliżonego planktonu. */
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
