package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.*;
import pl.simulation.ocean.util.Position;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SharkTurnHandler {

    private final Random random;
    private final boolean verbose;

    public SharkTurnHandler(Random random) {
        this(random, true);
    }

    public SharkTurnHandler(Random random, boolean verbose) {
        this.random = random;
        this.verbose = verbose;
    }

    public void executeTurn(Shark shark, Ocean ocean) {
        if (!shark.isAlive())
            return;

        for (int move = 0; move < LivingEntity.MAX_MOVES_PER_TURN; move++) {
            if (!shark.isAlive())
                break;

            MovementStrategy strategy = chooseStrategy(shark, ocean);
            Position next = strategy.nextPosition(shark, ocean);
            shark.setPosition(next);
            shark.consumeMoveEnergy();

            checkFishAttack(shark, ocean);

            checkPlanktonEaten(shark, ocean);
        }
    }

    private MovementStrategy chooseStrategy(Shark shark, Ocean ocean) {
        Position pos = shark.getPosition();

        Optional<Fish> nearestFish = ocean.getLiveFish().stream()
                .filter(f -> pos.distanceTo(f.getPosition()) <= Shark.DETECTION_RANGE)
                .min(Comparator.comparingDouble(f -> pos.distanceTo(f.getPosition())));

        if (nearestFish.isPresent()) {
            return new MoveToTargetStrategy(nearestFish.get().getPosition());
        }

        Optional<Plankton> nearestPlankton = ocean.getLivePlankton().stream()
                .filter(p -> pos.distanceTo(p.getPosition()) <= Shark.DETECTION_RANGE)
                .min(Comparator.comparingDouble(p -> pos.distanceTo(p.getPosition())));

        if (nearestPlankton.isPresent()) {
            return new MoveToTargetStrategy(nearestPlankton.get().getPosition());
        }

        return new RandomMovementStrategy(random);
    }

    private void checkFishAttack(Shark shark, Ocean ocean) {
        List<Fish> fishList = ocean.getLiveFish();
        for (Fish fish : fishList) {
            if (shark.getPosition().equals(fish.getPosition())) {
                shark.attack(fish);
                if (verbose) {
                    System.out.println("  " + shark.getName() + " zaatakował " + fish.getName()
                            + " na " + shark.getPosition()
                            + " | energia rybki: " + fish.getEnergy()
                            + " | energia rekina: " + shark.getEnergy());
                    if (!fish.isAlive()) {
                        System.out.println("  " + fish.getName() + " zginęła!");
                    }
                }
                break;
            }
        }
    }

    private void checkPlanktonEaten(Shark shark, Ocean ocean) {
        List<Plankton> planktonList = ocean.getLivePlankton();
        for (Plankton plankton : planktonList) {
            if (shark.getPosition().equals(plankton.getPosition())) {
                plankton.eat();
                shark.gainEnergy(Shark.PLANKTON_ENERGY);
                if (verbose) {
                    System.out.println("  " + shark.getName() + " zjadł plankton na " + shark.getPosition()
                            + " | energia: " + shark.getEnergy());
                }
                break;
            }
        }
    }
}
