package pl.simulation.ocean;

import pl.simulation.ocean.logic.Simulation;
import pl.simulation.ocean.view.SimulationWindow;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        boolean gui = false;
        Long seedArg = null;

        for (String arg : args) {
            if ("--gui".equals(arg) || "-g".equals(arg)) {
                gui = true;
            } else {
                try {
                    seedArg = Long.parseLong(arg);
                } catch (NumberFormatException ignored) {
                    System.err.println("Nieznany argument: " + arg);
                }
            }
        }

        long seed = seedArg != null ? seedArg : System.currentTimeMillis();
        System.out.println("Seed: " + seed);

        if (gui) {
            SimulationWindow.show(s -> new Simulation(new Random(s), false), seed);
        } else {
            Simulation simulation = new Simulation(new Random(seed));
            simulation.run();
        }
    }
}
