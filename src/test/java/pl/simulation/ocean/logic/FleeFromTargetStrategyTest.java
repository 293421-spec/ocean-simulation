package pl.simulation.ocean.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.Fish;
import pl.simulation.ocean.model.Ocean;
import pl.simulation.ocean.util.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy strategii {@link pl.simulation.ocean.logic.FleeFromTargetStrategy}.
 * Strategia przesuwa organizm o jeden krok w kierunku przeciwnym do zagrożenia.
 * Testy pokrywają ucieczkę prostoliniową, ukośną, zaciśnięcie do granic planszy
 * i brak ruchu gdy ucieczka prowadziłaby poza planszę.
 */
class FleeFromTargetStrategyTest {

    private Ocean ocean;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
    }

    /** Zagrożenie bezpośrednio powyżej — rybka ucieka o jeden krok w dół. */
    @Test
    void movesAwayFromThreat() {
        Fish fish = new Fish("Rybka1", 10, 10);
        FleeFromTargetStrategy strategy = new FleeFromTargetStrategy(new Position(10, 14));

        Position next = strategy.nextPosition(fish, ocean);

        assertEquals(new Position(10, 9), next);
    }

    /** Zagrożenie po przekątnej — rybka ucieka ukośnie w przeciwnym kierunku. */
    @Test
    void movesDiagonallyAwayWhenThreatIsDiagonal() {
        Fish fish = new Fish("Rybka1", 10, 10);
        FleeFromTargetStrategy strategy = new FleeFromTargetStrategy(new Position(12, 12));

        Position next = strategy.nextPosition(fish, ocean);

        assertEquals(new Position(9, 9), next);
    }

    /** Ucieczka z narożnika (0,0) od zagrożenia po przekątnej nie wychodzi poza planszę. */
    @Test
    void clampsToBoundsWhenFleeWouldLeaveOcean() {
        Fish fish = new Fish("Rybka1", 0, 0);
        FleeFromTargetStrategy strategy = new FleeFromTargetStrategy(new Position(5, 5));

        Position next = strategy.nextPosition(fish, ocean);

        assertTrue(ocean.isWithinBounds(next));
        assertNotEquals(new Position(-1, -1), next);
    }

    /** Rybka w narożniku z zagrożeniem bezpośrednio obok zostaje na miejscu, gdy jedyny kierunek ucieczki wychodzi poza planszę. */
    @Test
    void staysInPlaceWhenCorneredWithNoValidFleeCell() {
        Fish fish = new Fish("Rybka1", 0, 0);
        FleeFromTargetStrategy strategy = new FleeFromTargetStrategy(new Position(0, 1));

        Position next = strategy.nextPosition(fish, ocean);

        assertEquals(new Position(0, 0), next);
    }
}
