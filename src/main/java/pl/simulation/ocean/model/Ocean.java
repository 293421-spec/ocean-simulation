package pl.simulation.ocean.model;

import pl.simulation.ocean.util.Position;

import java.util.ArrayList;
import java.util.List;

public class Ocean {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    private final List<Fish> fish;
    private final List<Shark> sharks;
    private final List<Plankton> planktons;

    public Ocean() {
        this.fish = new ArrayList<>();
        this.sharks = new ArrayList<>();
        this.planktons = new ArrayList<>();
    }

    public void addFish(Fish f) {
        fish.add(f);
    }

    public void addShark(Shark s) {
        sharks.add(s);
    }

    public void addPlankton(Plankton p) {
        planktons.add(p);
    }

    public List<Fish> getFish() {
        return fish;
    }

    public List<Shark> getSharks() {
        return sharks;
    }

    public List<Plankton> getPlanktons() {
        return planktons;
    }

    public List<Fish> getLiveFish() {
        return fish.stream().filter(Fish::isAlive).collect(java.util.stream.Collectors.toList());
    }

    public List<Shark> getLiveSharks() {
        return sharks.stream().filter(Shark::isAlive).collect(java.util.stream.Collectors.toList());
    }

    public List<Plankton> getLivePlankton() {
        return planktons.stream().filter(Plankton::isAlive).collect(java.util.stream.Collectors.toList());
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public boolean isWithinBounds(Position pos) {
        return isWithinBounds(pos.getX(), pos.getY());
    }

    public boolean isSimulationOver() {
        return getLiveFish().isEmpty();
    }

    public void printStatus(int turn) {
        System.out.println("=== Tura " + turn + " ===");
        System.out.println("  Żywe rybki:   " + getLiveFish().size());
        System.out.println("  Żywe rekiny:  " + getLiveSharks().size());
        System.out.println("  Żywy plankton:" + getLivePlankton().size());
        System.out.println();
    }
}
