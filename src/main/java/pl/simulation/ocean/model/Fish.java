package pl.simulation.ocean.model;

public class Fish extends LivingEntity {

    public static final double DETECTION_RANGE = 5.0;
    public static final int PLANKTON_ENERGY = 10;

    private String name;

    public Fish(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "@" + position + "[energia=" + energy + "]";
    }
}
