package pl.simulation.ocean.logic;

import pl.simulation.ocean.model.*;
import pl.simulation.ocean.util.Position;

public final class SimulationSnapshot {

    private final int turnNumber;
    private final int[][] fishState;
    private final int[][] sharkState;
    private final int[][] planktonState;

    private SimulationSnapshot(int turnNumber, int[][] fishState, int[][] sharkState, int[][] planktonState) {
        this.turnNumber = turnNumber;
        this.fishState = fishState;
        this.sharkState = sharkState;
        this.planktonState = planktonState;
    }

    public static SimulationSnapshot capture(Simulation simulation) {
        Ocean ocean = simulation.getOcean();
        int[][] fish = new int[ocean.getFish().size()][3];
        int i = 0;
        for (Fish f : ocean.getFish()) {
            Position p = f.getPosition();
            fish[i][0] = p.getX();
            fish[i][1] = p.getY();
            fish[i][2] = f.getEnergy();
            i++;
        }
        int[][] sharks = new int[ocean.getSharks().size()][3];
        i = 0;
        for (Shark s : ocean.getSharks()) {
            Position p = s.getPosition();
            sharks[i][0] = p.getX();
            sharks[i][1] = p.getY();
            sharks[i][2] = s.getEnergy();
            i++;
        }
        int[][] plankton = new int[ocean.getPlanktons().size()][3];
        i = 0;
        for (Plankton pl : ocean.getPlanktons()) {
            Position p = pl.getPosition();
            plankton[i][0] = p.getX();
            plankton[i][1] = p.getY();
            plankton[i][2] = pl.isAlive() ? 0 : 1;
            i++;
        }
        return new SimulationSnapshot(simulation.getTurnNumber(), fish, sharks, plankton);
    }

    public void restore(Simulation simulation) {
        simulation.setTurnNumber(turnNumber);
        Ocean ocean = simulation.getOcean();
        int i = 0;
        for (Fish f : ocean.getFish()) {
            f.restoreState(fishState[i][0], fishState[i][1], fishState[i][2]);
            i++;
        }
        i = 0;
        for (Shark s : ocean.getSharks()) {
            s.restoreState(sharkState[i][0], sharkState[i][1], sharkState[i][2]);
            i++;
        }
        i = 0;
        for (Plankton pl : ocean.getPlanktons()) {
            pl.restoreState(planktonState[i][0], planktonState[i][1], planktonState[i][2] == 1);
            i++;
        }
    }
}
