package pl.simulation.ocean.logic;

import org.junit.jupiter.api.Test;
import pl.simulation.ocean.model.*;
import pl.simulation.ocean.testutil.SystemOutSilencer;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy klasy {@link pl.simulation.ocean.logic.OceanInitializer}.
 * Weryfikują, że inicjalizacja oceanu tworzy dokładnie 4 rekiny, 8 rybek i 12 planktonów,
 * wszystkie żywe z pełną energią, na unikalnych komórkach w granicach planszy,
 * z polskimi nazwami sekwencyjnymi oraz deterministycznym układem przy stałym ziarnie.
 */
class OceanInitializerTest {

    /** Po inicjalizacji ocean zawiera dokładnie 4 rekiny, 8 rybek i 12 planktonów. */
    @Test
    void initializeSpawnsExpectedCounts() throws Exception {
        Ocean ocean = new Ocean();
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            new OceanInitializer(new Random(42L)).initialize(ocean);
        }

        assertEquals(4, ocean.getSharks().size());
        assertEquals(8, ocean.getFish().size());
        assertEquals(12, ocean.getPlanktons().size());
    }

    /** Wszystkie rybki i rekiny startują z pełną energią (100) i są żywe; plankton też jest żywy. */
    @Test
    void allSpawnedEntitiesStartAliveWithFullEnergy() throws Exception {
        Ocean ocean = new Ocean();
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            new OceanInitializer(new Random(7L)).initialize(ocean);
        }

        ocean.getFish().forEach(f -> {
            assertTrue(f.isAlive());
            assertEquals(LivingEntity.INITIAL_ENERGY, f.getEnergy());
        });
        ocean.getSharks().forEach(s -> {
            assertTrue(s.isAlive());
            assertEquals(LivingEntity.INITIAL_ENERGY, s.getEnergy());
        });
        ocean.getPlanktons().forEach(p -> assertTrue(p.isAlive()));
    }

    /** Wszystkie 24 organizmy (4+8+12) zajmują odrębne komórki wewnątrz planszy 20×20. */
    @Test
    void spawnPositionsAreUniqueAndWithinBounds() throws Exception {
        Ocean ocean = new Ocean();
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            new OceanInitializer(new Random(99L)).initialize(ocean);
        }

        Set<String> keys = new HashSet<>();
        assertOccupied(ocean.getSharks(), keys);
        assertOccupied(ocean.getFish(), keys);
        assertOccupied(ocean.getPlanktons(), keys);

        assertEquals(4 + 8 + 12, keys.size());
    }

    /** Pierwszy rekin ma nazwę "Rekin1", pierwsza rybka — "Rybka1" (nazwy numerowane od 1). */
    @Test
    void sharksAndFishReceiveSequentialPolishNames() throws Exception {
        Ocean ocean = new Ocean();
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            new OceanInitializer(new Random(1L)).initialize(ocean);
        }

        assertEquals("Rekin1", ocean.getSharks().get(0).getName());
        assertEquals("Rybka1", ocean.getFish().get(0).getName());
    }

    /** To samo ziarno generuje identyczny układ startowy — pozycje pierwszej rybki i rekina są równe. */
    @Test
    void sameSeedProducesSameLayout() throws Exception {
        Ocean first = new Ocean();
        Ocean second = new Ocean();
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            new OceanInitializer(new Random(555L)).initialize(first);
            new OceanInitializer(new Random(555L)).initialize(second);
        }

        assertEquals(first.getFish().get(0).getPosition(), second.getFish().get(0).getPosition());
        assertEquals(first.getSharks().get(0).getPosition(), second.getSharks().get(0).getPosition());
    }

    private static void assertOccupied(Iterable<? extends pl.simulation.ocean.model.Entity> entities, Set<String> keys) {
        for (var entity : entities) {
            var pos = entity.getPosition();
            assertTrue(pos.getX() >= 0 && pos.getX() < Ocean.WIDTH);
            assertTrue(pos.getY() >= 0 && pos.getY() < Ocean.HEIGHT);
            assertTrue(keys.add(pos.getX() + "," + pos.getY()));
        }
    }
}
