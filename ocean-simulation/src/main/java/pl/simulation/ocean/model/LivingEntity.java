package pl.simulation.ocean.model;

public abstract class LivingEntity extends Entity {

    public static final int INITIAL_ENERGY = 100;
    public static final int MOVE_ENERGY_COST = 5;
    public static final int MAX_MOVES_PER_TURN = 5;

    protected int energy;

    public LivingEntity(int x, int y) {
        super(x, y);
        this.energy = INITIAL_ENERGY;
    }

    public int getEnergy() {
        return energy;
    }

    public void gainEnergy(int amount) {
        this.energy += amount;
    }

    public void loseEnergy(int amount) {
        this.energy -= amount;
    }

    public boolean consumeMoveEnergy() {
        if (!isAlive())
            return false;
        this.energy -= MOVE_ENERGY_COST;
        return true;
    }

    @Override
    public boolean isAlive() {
        return energy > 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + position + "[energia=" + energy + "]";
    }
}
