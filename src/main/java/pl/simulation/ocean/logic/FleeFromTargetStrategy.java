package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.LivingEntity;
import pl.simulation.ocean.model.Ocean;
import pl.simulation.ocean.util.Position;

public class FleeFromTargetStrategy implements MovementStrategy {

    private final Position threat;

    public FleeFromTargetStrategy(Position threat) {
        this.threat = threat;
    }

    @Override
    public Position nextPosition(LivingEntity entity, Ocean ocean) {
        Position current = entity.getPosition();
        int cx = current.getX();
        int cy = current.getY();

        int dx = Integer.signum(cx - threat.getX());
        int dy = Integer.signum(cy - threat.getY());

        int nx = cx + dx;
        int ny = cy + dy;

        if (!ocean.isWithinBounds(nx, ny)) {
            if (ocean.isWithinBounds(nx, cy)) {
                ny = cy;
            } else if (ocean.isWithinBounds(cx, ny)) {
                nx = cx;
            } else {
                return new Position(cx, cy);
            }
        }

        return new Position(nx, ny);
    }
}
