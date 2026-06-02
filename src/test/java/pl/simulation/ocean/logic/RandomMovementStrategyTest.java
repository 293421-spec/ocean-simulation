package pl.simulation.ocean.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.Fish;
import pl.simulation.ocean.model.Ocean;
import pl.simulation.ocean.util.Position;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy strategii {@link pl.simulation.ocean.logic.RandomMovementStrategy}.
 * Weryfikują, że losowy ruch zawsze mieści się w granicach planszy,
 * jest deterministyczny przy stałym ziarnie, ograniczony do sąsiednich komórek
 * i poprawnie obsługuje narożniki planszy.
 */
class RandomMovementStrategyTest {

    private Ocean ocean;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
    }

    /** Po 50 losowych krokach pozycja nigdy nie wychodzi poza planszę 20×20. */
    @Test
    void nextPositionStaysWithinBounds() {
        Fish fish = new Fish("Rybka1", 10, 10);
        RandomMovementStrategy strategy = new RandomMovementStrategy(new Random(42));

        for (int i = 0; i < 50; i++) {
            Position next = strategy.nextPosition(fish, ocean);
            assertTrue(ocean.isWithinBounds(next));
            fish.setPosition(next);
        }
    }

    /** Dwie strategie z tym samym ziarnem losowania zwracają identyczny pierwszy krok. */
    @Test
    void seededRandomProducesDeterministicStep() {
        Fish fish = new Fish("Rybka1", 5, 5);
        RandomMovementStrategy strategy = new RandomMovementStrategy(new Random(12345L));

        Position first = strategy.nextPosition(fish, ocean);
        Position second = new RandomMovementStrategy(new Random(12345L))
                .nextPosition(new Fish("Rybka2", 5, 5), ocean);

        assertEquals(first, second);
    }

    /** Z narożnika (0,0) dostępne są tylko 3 sąsiednie komórki — strategia wybiera jedną z nich. */
    @Test
    void cornerCellHasOnlyValidNeighborDirections() {
        Fish fish = new Fish("Rybka1", 0, 0);
        RandomMovementStrategy strategy = new RandomMovementStrategy(new Random(0));

        Position next = strategy.nextPosition(fish, ocean);

        assertTrue(
            next.equals(new Position(1, 0)) ||
            next.equals(new Position(0, 1)) ||
            next.equals(new Position(1, 1))
        );
    }

    /** Jeden krok przesuwa organizm maksymalnie o 1 komórkę w każdym z kierunków (w tym przekątne). */
    @Test
    void moveChangesAtMostOneCellPerAxis() {
        Fish fish = new Fish("Rybka1", 7, 7);
        Position before = fish.getPosition();
        Position next = new RandomMovementStrategy(new Random(99)).nextPosition(fish, ocean);

        assertTrue(Math.abs(next.getX() - before.getX()) <= 1);
        assertTrue(Math.abs(next.getY() - before.getY()) <= 1);
    }
}
