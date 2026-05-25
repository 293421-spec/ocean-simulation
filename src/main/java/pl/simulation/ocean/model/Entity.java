package pl.simulation.ocean.model;

import pl.simulation.ocean.util.Position;

public abstract class Entity {

    protected Position position;

    public Entity(int x, int y) {
        this.position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract boolean isAlive();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + position;
    }
}
