package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.LivingEntity;
import pl.simulation.ocean.model.Ocean;
import pl.simulation.ocean.util.Position;

public class MoveToTargetStrategy implements MovementStrategy {

    private final Position target;

    public MoveToTargetStrategy(Position target) {
        this.target = target;
    }

    @Override
    public Position nextPosition(LivingEntity entity, Ocean ocean) {
        Position current = entity.getPosition();
        int cx = current.getX();
        int cy = current.getY();

        int dx = Integer.signum(target.getX() - cx);
        int dy = Integer.signum(target.getY() - cy);

        // Preferuj ruch po osi z większą różnicą
        int absDx = Math.abs(target.getX() - cx);
        int absDy = Math.abs(target.getY() - cy);

        int nx = cx;
        int ny = cy;

        if (absDx >= absDy && ocean.isWithinBounds(cx + dx, cy)) {
            nx = cx + dx;
        } else if (absDy > 0 && ocean.isWithinBounds(cx, cy + dy)) {
            ny = cy + dy;
        } else if (absDx > 0 && ocean.isWithinBounds(cx + dx, cy)) {
            nx = cx + dx;
        }

        return new Position(nx, ny);
    }
}
