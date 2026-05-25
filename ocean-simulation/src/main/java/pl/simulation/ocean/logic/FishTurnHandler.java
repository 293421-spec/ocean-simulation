package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.*;
import pl.simulation.ocean.util.Position;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FishTurnHandler {

    private final Random random;

    public FishTurnHandler(Random random) {
        this.random = random;
    }

    public void executeTurn(Fish fish, Ocean ocean) {
        if (!fish.isAlive())
            return;

        for (int move = 0; move < LivingEntity.MAX_MOVES_PER_TURN; move++) {
            if (!fish.isAlive())
                break;

            MovementStrategy strategy = chooseStrategy(fish, ocean);
            Position next = strategy.nextPosition(fish, ocean);
            fish.setPosition(next);
            fish.consumeMoveEnergy();

            checkPlanktonEaten(fish, ocean);
        }
    }

    private MovementStrategy chooseStrategy(Fish fish, Ocean ocean) {
        Position pos = fish.getPosition();

        Optional<Shark> nearestShark = ocean.getLiveSharks().stream()
                .filter(s -> pos.distanceTo(s.getPosition()) <= Fish.DETECTION_RANGE)
                .min(Comparator.comparingDouble(s -> pos.distanceTo(s.getPosition())));

        if (nearestShark.isPresent()) {
            return new FleeFromTargetStrategy(nearestShark.get().getPosition());
        }

        Optional<Plankton> nearestPlankton = ocean.getLivePlankton().stream()
                .filter(p -> pos.distanceTo(p.getPosition()) <= Fish.DETECTION_RANGE)
                .min(Comparator.comparingDouble(p -> pos.distanceTo(p.getPosition())));

        if (nearestPlankton.isPresent()) {
            return new MoveToTargetStrategy(nearestPlankton.get().getPosition());
        }

        return new RandomMovementStrategy(random);
    }

    private void checkPlanktonEaten(Fish fish, Ocean ocean) {
        List<Plankton> planktonList = ocean.getLivePlankton();
        for (Plankton plankton : planktonList) {
            if (fish.getPosition().equals(plankton.getPosition())) {
                plankton.eat();
                fish.gainEnergy(Fish.PLANKTON_ENERGY);
                System.out.println("  " + fish.getName() + " zjadła plankton na " + fish.getPosition()
                        + " | energia: " + fish.getEnergy());
                break;
            }
        }
    }
}
