package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OceanInitializer {

    private static final int SHARK_COUNT = 4;
    private static final int FISH_COUNT = 8;
    private static final int PLANKTON_COUNT = 12;

    private final Random random;

    public OceanInitializer(Random random) {
        this.random = random;
    }

    public void initialize(Ocean ocean) {
        Set<String> usedPositions = new HashSet<>();

        for (int i = 1; i <= SHARK_COUNT; i++) {
            int[] pos = randomFreePosition(usedPositions, ocean);
            ocean.addShark(new Shark("Rekin" + i, pos[0], pos[1]));
        }

        for (int i = 1; i <= FISH_COUNT; i++) {
            int[] pos = randomFreePosition(usedPositions, ocean);
            ocean.addFish(new Fish("Rybka" + i, pos[0], pos[1]));
        }

        for (int i = 1; i <= PLANKTON_COUNT; i++) {
            int[] pos = randomFreePosition(usedPositions, ocean);
            ocean.addPlankton(new Plankton(pos[0], pos[1]));
        }

        System.out.println("- Inicjalizacja oceanu -");
        System.out.println("  Rekiny:   " + ocean.getSharks().size());
        System.out.println("  Rybki:    " + ocean.getFish().size());
        System.out.println("  Plankton: " + ocean.getPlanktons().size());
        System.out.println();
    }

    private int[] randomFreePosition(Set<String> used, Ocean ocean) {
        int x, y;
        String key;
        do {
            x = random.nextInt(Ocean.WIDTH);
            y = random.nextInt(Ocean.HEIGHT);
            key = x + "," + y;
        } while (used.contains(key));
        used.add(key);
        return new int[] { x, y };
    }
}
