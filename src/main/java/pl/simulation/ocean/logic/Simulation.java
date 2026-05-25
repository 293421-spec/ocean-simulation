package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.*;

import java.util.List;
import java.util.Random;

public class Simulation {

    private final Ocean ocean;
    private final FishTurnHandler fishHandler;
    private final SharkTurnHandler sharkHandler;
    private int turnNumber = 0;

    public Simulation(Random random) {
        this.ocean = new Ocean();
        this.fishHandler = new FishTurnHandler(random);
        this.sharkHandler = new SharkTurnHandler(random);

        OceanInitializer initializer = new OceanInitializer(random);
        initializer.initialize(ocean);
    }

    public void run() {
        System.out.println("- Start symulacji -\n");

        while (!ocean.isSimulationOver()) {
            turnNumber++;
            System.out.println("- Tura " + turnNumber + " -");

            List<Fish> liveFish = ocean.getLiveFish();
            for (Fish fish : liveFish) {
                fishHandler.executeTurn(fish, ocean);
            }

            List<Shark> liveSharks = ocean.getLiveSharks();
            for (Shark shark : liveSharks) {
                sharkHandler.executeTurn(shark, ocean);
            }

            ocean.printStatus(turnNumber);
        }

        printFinalStats();
    }

    private void printFinalStats() {
        System.out.println("- Koniec symulacji po " + turnNumber + " turach -");
        System.out.println("  Żywe rekiny:  " + ocean.getLiveSharks().size());
        System.out.println("  Żywy plankton:" + ocean.getLivePlankton().size());
        System.out.println("  Wszystkie rybki zginęły.");
    }

    public Ocean getOcean() {
        return ocean;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
}
