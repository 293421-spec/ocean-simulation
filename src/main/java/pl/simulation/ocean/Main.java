package pl.simulation.ocean;

import pl.simulation.ocean.logic.Simulation;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        long seed = args.length > 0 ? Long.parseLong(args[0]) : System.currentTimeMillis();
        System.out.println("Seed: " + seed);

        Simulation simulation = new Simulation(new Random(seed));
        simulation.run();
    }
}
