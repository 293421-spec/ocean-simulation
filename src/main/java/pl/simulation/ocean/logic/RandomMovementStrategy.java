package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.LivingEntity;
import pl.simulation.ocean.model.Ocean;
import pl.simulation.ocean.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMovementStrategy implements MovementStrategy {

    private static final int[][] DIRECTIONS = {
            { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 },
            { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }
    };

    private final Random random;

    public RandomMovementStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Position nextPosition(LivingEntity entity, Ocean ocean) {
        Position current = entity.getPosition();
        int cx = current.getX();
        int cy = current.getY();

        List<Position> valid = new ArrayList<>();
        for (int[] dir : DIRECTIONS) {
            int nx = cx + dir[0];
            int ny = cy + dir[1];
            if (ocean.isWithinBounds(nx, ny)) {
                valid.add(new Position(nx, ny));
            }
        }

        if (valid.isEmpty())
            return new Position(cx, cy);
        return valid.get(random.nextInt(valid.size()));
    }
}
