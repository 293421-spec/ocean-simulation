package pl.simulation.ocean.model;

public class Plankton extends Entity {

    private boolean eaten;

    public Plankton(int x, int y) {
        super(x, y);
        this.eaten = false;
    }

    public void eat() {
        this.eaten = true;
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
