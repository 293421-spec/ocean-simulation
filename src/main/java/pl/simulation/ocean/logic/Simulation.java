package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.*;

import javax.swing.*;
import java.util.List;
import java.util.Random;

public class Simulation {

    private Ocean ocean;
    private final FishTurnHandler fishHandler;
    private final SharkTurnHandler sharkHandler;
    private int turnNumber = 0;

    public Simulation(Random random) {
        this(random, true);
    }

    public Simulation(Random random, boolean verbose) {
        this.ocean = new Ocean();
        this.fishHandler = new FishTurnHandler(random, verbose);
        this.sharkHandler = new SharkTurnHandler(random, verbose);

        OceanInitializer initializer = new OceanInitializer(random);
        initializer.initialize(ocean, verbose);
    }

    public void run() {
        System.out.println("- Start symulacji -\n");

        while (executeNextTurn(true)) {}

        printFinalStats();
    }

    public boolean executeNextTurn(boolean verbose) {
        if (ocean.isSimulationOver()) {
            return false;
        }

        turnNumber++;
        if (verbose) {
            System.out.println("- Tura " + turnNumber + " -");
        }

        List<Fish> liveFish = ocean.getLiveFish();
        for (Fish fish : liveFish) {
            fishHandler.executeTurn(fish, ocean);
        }

        List<Shark> liveSharks = ocean.getLiveSharks();
        for (Shark shark : liveSharks) {
            sharkHandler.executeTurn(shark, ocean);
        }

        if (verbose) {
            ocean.printStatus(turnNumber);
        }

        return !ocean.isSimulationOver();
    }

    public boolean isFinished() {
        return ocean.isSimulationOver();
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

    void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }
}
