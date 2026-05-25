package pl.simulation.ocean.model;

import pl.simulation.ocean.util.Position;

public class Plankton extends Entity {

    private boolean eaten;

    public Plankton(int x, int y) {
        super(x, y);
        this.eaten = false;
    }

    public void eat() {
        this.eaten = true;
    }

    public void restoreState(int x, int y, boolean eaten) {
        this.position = new Position(x, y);
        this.eaten = eaten;
    }

    @Override
    public boolean isAlive() {
        return !eaten;
    }

    @Override
    public String toString() {
        return "Plankton@" + position + (eaten ? "[zjedzony]" : "");
    }
}
