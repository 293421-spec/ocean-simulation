package pl.simulation.ocean.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.simulation.ocean.testutil.SystemOutSilencer;
import pl.simulation.ocean.util.Position;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy klasy {@link pl.simulation.ocean.model.Ocean}.
 * Sprawdzają wymiary planszy, rejestrację organizmów, sprawdzanie granic,
 * filtrowanie żywych/martwych jednostek, warunek końca symulacji
 * oraz treść komunikatu stanu tury.
 */
class OceanTest {

    private Ocean ocean;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
    }

    /** Wymiary planszy muszą wynosić dokładnie 20×20. */
    @Test
    void boardDimensionsAreTwentyByTwenty() {
        assertEquals(20, Ocean.WIDTH);
        assertEquals(20, Ocean.HEIGHT);
    }

    /** Nowo utworzony ocean nie zawiera żadnych organizmów. */
    @Test
    void startsEmpty() {
        assertTrue(ocean.getFish().isEmpty());
        assertTrue(ocean.getSharks().isEmpty());
        assertTrue(ocean.getPlanktons().isEmpty());
    }

    /** Metody {@code addFish}, {@code addShark}, {@code addPlankton} umieszczają organizmy w odpowiednich listach. */
    @Test
    void addMethodsRegisterEntitiesInLists() {
        Fish fish = new Fish("Rybka1", 1, 1);
        Shark shark = new Shark("Rekin1", 2, 2);
        Plankton plankton = new Plankton(3, 3);

        ocean.addFish(fish);
        ocean.addShark(shark);
        ocean.addPlankton(plankton);

        assertEquals(1, ocean.getFish().size());
        assertEquals(1, ocean.getSharks().size());
        assertEquals(1, ocean.getPlanktons().size());
        assertSame(fish, ocean.getFish().get(0));
    }

    /** Komórki od (0,0) do (19,19) są w granicach; współrzędne ujemne i ≥ 20 — nie. */
    @Test
    void isWithinBoundsAcceptsValidCellsOnly() {
        assertTrue(ocean.isWithinBounds(0, 0));
        assertTrue(ocean.isWithinBounds(19, 19));
        assertTrue(ocean.isWithinBounds(new Position(10, 10)));

        assertFalse(ocean.isWithinBounds(20, 0));
        assertFalse(ocean.isWithinBounds(0, 20));
        assertFalse(ocean.isWithinBounds(-1, 5));
        assertFalse(ocean.isWithinBounds(5, -1));
        assertFalse(ocean.isWithinBounds(new Position(-1, 0)));
    }

    /**
     * {@code getLiveFish}, {@code getLiveSharks} i {@code getLivePlankton} zwracają tylko żywe jednostki —
     * martwe rybki/rekiny (energia ≤ 0) i zjedzony plankton są pomijane.
     */
    @Test
    void getLiveFiltersDeadEntities() {
        Fish alive = new Fish("A", 0, 0);
        Fish dead = new Fish("B", 1, 1);
        dead.loseEnergy(100);

        Shark liveShark = new Shark("S1", 2, 2);
        Shark deadShark = new Shark("S2", 3, 3);
        deadShark.loseEnergy(100);

        Plankton livePlankton = new Plankton(4, 4);
        Plankton eaten = new Plankton(5, 5);
        eaten.eat();

        ocean.addFish(alive);
        ocean.addFish(dead);
        ocean.addShark(liveShark);
        ocean.addShark(deadShark);
        ocean.addPlankton(livePlankton);
        ocean.addPlankton(eaten);

        assertEquals(1, ocean.getLiveFish().size());
        assertEquals(1, ocean.getLiveSharks().size());
        assertEquals(1, ocean.getLivePlankton().size());
        assertSame(alive, ocean.getLiveFish().get(0));
    }

    /** Symulacja kończy się, gdy nie ma żywych rybek; dołączenie żywej rybki wznawia symulację. */
    @Test
    void isSimulationOverWhenNoLiveFish() {
        assertTrue(ocean.isSimulationOver());

        Fish fish = new Fish("Rybka1", 1, 1);
        ocean.addFish(fish);
        assertFalse(ocean.isSimulationOver());

        fish.loseEnergy(100);
        assertTrue(ocean.isSimulationOver());
    }

    /** Lista z wyłączniem martwej rybki jest traktowana jak pusta — symulacja jest skończona. */
    @Test
    void isSimulationOverIgnoresDeadFishInList() {
        Fish dead = new Fish("Rybka1", 0, 0);
        dead.loseEnergy(100);
        ocean.addFish(dead);
        assertTrue(ocean.isSimulationOver());
    }

    /** {@code printStatus} wypisuje numer tury oraz liczby żywych rybek, rekinów i planktonu. */
    @Test
    void printStatusWritesTurnSummary() throws Exception {
        ocean.addFish(new Fish("Rybka1", 0, 0));
        ocean.addShark(new Shark("Rekin1", 1, 1));

        String output;
        try (SystemOutSilencer silencer = new SystemOutSilencer()) {
            ocean.printStatus(3);
            output = silencer.captured();
        }

        assertTrue(output.contains("Tura 3"));
        assertTrue(output.contains("Żywe rybki"));
        assertTrue(output.contains("Żywe rekiny"));
        assertTrue(output.contains("Żywy plankton"));
    }
}
