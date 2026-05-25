package pl.simulation.ocean.model;

public class Shark extends LivingEntity {

    public static final double DETECTION_RANGE = 7.0;
    public static final int ATTACK_DAMAGE = 20;
    public static final int ATTACK_ENERGY_GAIN = 20;
    public static final int PLANKTON_ENERGY = 10;

    private String name;

    public Shark(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void attack(Fish fish) {
        fish.loseEnergy(ATTACK_DAMAGE);
        this.gainEnergy(ATTACK_ENERGY_GAIN);
    }

    @Override
    public String toString() {
        return name + "@" + position + "[energia=" + energy + "]";
    }
}
