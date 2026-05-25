package pl.simulation.ocean.logic;

import org.junit.jupiter.api.Test;
import pl.simulation.ocean.testutil.SystemOutSilencer;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void constructorInitializesOceanWithEntities() throws Exception {
        Simulation simulation;
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            simulation = new Simulation(new Random(12345L));
        }

        assertFalse(simulation.getOcean().getFish().isEmpty());
        assertFalse(simulation.getOcean().getSharks().isEmpty());
        assertFalse(simulation.getOcean().getPlanktons().isEmpty());
        assertEquals(0, simulation.getTurnNumber());
    }

    @Test
    void runEndsWhenAllFishAreDead() throws Exception {
        Simulation simulation;
        String output;
        try (SystemOutSilencer silencer = new SystemOutSilencer()) {
            simulation = new Simulation(new Random(12345L));
            simulation.run();
            output = silencer.captured();
        }

        assertTrue(simulation.getOcean().isSimulationOver());
        assertTrue(simulation.getTurnNumber() > 0);
        assertTrue(output.contains("Koniec symulacji"));
        assertTrue(output.contains("Wszystkie rybki zginęły"));
    }

    @Test
    void runIsDeterministicForFixedSeed() throws Exception {
        int turnsFirst;
        int liveSharksFirst;
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            Simulation sim = new Simulation(new Random(999L));
            sim.run();
            turnsFirst = sim.getTurnNumber();
            liveSharksFirst = sim.getOcean().getLiveSharks().size();
        }

        int turnsSecond;
        int liveSharksSecond;
        try (SystemOutSilencer ignored = new SystemOutSilencer()) {
            Simulation sim = new Simulation(new Random(999L));
            sim.run();
            turnsSecond = sim.getTurnNumber();
            liveSharksSecond = sim.getOcean().getLiveSharks().size();
        }

        assertEquals(turnsFirst, turnsSecond);
        assertEquals(liveSharksFirst, liveSharksSecond);
    }

    @Test
    void fishPhasePrecedesSharksInTurnOrder() throws Exception {
        try (SystemOutSilencer silencer = new SystemOutSilencer()) {
            new Simulation(new Random(1L)).run();
            String output = silencer.captured();
            int firstTurn = output.indexOf("- Tura 1 -");
            assertTrue(firstTurn >= 0);
            String turnOne = output.substring(firstTurn, Math.min(output.length(), firstTurn + 800));
            int fishBlock = turnOne.indexOf("zjadła plankton");
            int sharkBlock = turnOne.indexOf("zaatakował");
            if (fishBlock >= 0 && sharkBlock >= 0) {
                assertTrue(fishBlock < sharkBlock, "Fish actions should appear before shark attacks in turn log");
            }
        }
    }
}
