package pl.simulation.ocean.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.Fish;
import pl.simulation.ocean.model.Ocean;
import pl.simulation.ocean.util.Position;

import static org.junit.jupiter.api.Assertions.*;

class MoveToTargetStrategyTest {

    private Ocean ocean;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
    }

    @Test
    void movesOneStepTowardTargetPreferringLargerAxisDelta() {
        Fish fish = new Fish("Rybka1", 5, 5);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(10, 10));

        Position next = strategy.nextPosition(fish, ocean);

        assertEquals(new Position(6, 5), next);
    }

    @Test
    void movesOnYWhenVerticalDistanceDominates() {
        Fish fish = new Fish("Rybka1", 5, 5);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(5, 12));

        Position next = strategy.nextPosition(fish, ocean);

        assertEquals(new Position(5, 6), next);
    }

    @Test
    void staysInPlaceWhenAlreadyOnTarget() {
        Fish fish = new Fish("Rybka1", 7, 7);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(7, 7));

        assertEquals(new Position(7, 7), strategy.nextPosition(fish, ocean));
    }

    @Test
    void doesNotLeaveBoardAtEdge() {
        Fish fish = new Fish("Rybka1", 0, 0);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(-5, -5));

        Position next = strategy.nextPosition(fish, ocean);

        assertTrue(ocean.isWithinBounds(next));
        assertEquals(new Position(0, 0), next);
    }

    @Test
    void advancesAlongXWhenYMoveBlockedAtBorder() {
        Fish fish = new Fish("Rybka1", 5, 19);
        MoveToTargetStrategy strategy = new MoveToTargetStrategy(new Position(5, 25));

        Position next = strategy.nextPosition(fish, ocean);

        assertEquals(new Position(5, 19), next);
    }
}
