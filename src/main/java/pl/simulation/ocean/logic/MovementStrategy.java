package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.LivingEntity;
import pl.simulation.ocean.model.Ocean;
import pl.simulation.ocean.util.Position;

public interface MovementStrategy {

    /**
     * @param entity poruszający się obiekt
     * @param ocean  referencja do planszy (granice, inne obiekty)
     * @return nowa pozycja (o jedno pole od obecnej) lub obecna jeśli brak ruchu
     */
    Position nextPosition(LivingEntity entity, Ocean ocean);
}
