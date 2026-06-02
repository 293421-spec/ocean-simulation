package pl.simulation.ocean.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.Fish;
import pl.simulation.ocean.model.Ocean;
import pl.simulation.ocean.util.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy strategii {@link pl.simulation.ocean.logic.MoveToTargetStrategy}.
 * Strategia przesuwa organizm o jeden krok w kierunku celu, preferując oś
 * z większą różnicą współrzędnych. Testy obejmują ruch poziomy, pionowy,
 * stanie w miejscu na celu oraz zachowanie przy granicy planszy.
 */
class MoveToTargetStrategyTest {

    private Ocean ocean;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
    }

    /**
     * Gdy delta X i Y są równe, strategia preferuje oś X — organizm przesuwa się o (1, 0)
     * zamiast (0, 1).
     */
    @Test
    void movesOneStepTowardTargetPreferringLargerAxisDelta() {
        Fish fish = new Fish("Rybka1", 5, 5);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(10, 10));

        Position next = strategy.nextPosition(fish, ocean);

        assertEquals(new Position(6, 5), next);
    }

    /** Gdy dystans pionowy dominuje, organizm przesuwa się wzdłuż osi Y. */
    @Test
    void movesOnYWhenVerticalDistanceDominates() {
        Fish fish = new Fish("Rybka1", 5, 5);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(5, 12));

        Position next = strategy.nextPosition(fish, ocean);

        assertEquals(new Position(5, 6), next);
    }

    /** Gdy organizm stoi już na celu, pozycja nie zmienia się. */
    @Test
    void staysInPlaceWhenAlreadyOnTarget() {
        Fish fish = new Fish("Rybka1", 7, 7);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(7, 7));

        assertEquals(new Position(7, 7), strategy.nextPosition(fish, ocean));
    }

    /** Cel poza planszą nie powoduje wyjścia organizmu — pozycja zostaje zaciskana do granic. */
    @Test
    void doesNotLeaveBoardAtEdge() {
        Fish fish = new Fish("Rybka1", 0, 0);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(-5, -5));

        Position next = strategy.nextPosition(fish, ocean);

        assertTrue(ocean.isWithinBounds(next));
        assertEquals(new Position(0, 0), next);
    }

    /** Gdy ruch wzdłuż preferowanej osi Y jest zablokowany przez krawędź, organizm stoi w miejscu (cel jest poza planszą). */
    @Test
    void advancesAlongXWhenYMoveBlockedAtBorder() {
        Fish fish = new Fish("Rybka1", 5, 19);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(5, 25));

        Position next = strategy.nextPosition(fish, ocean);

        assertEquals(new Position(5, 19), next);
    }
}
