package pl.simulation.ocean.model;

import org.junit.jupiter.api.Test;
import pl.simulation.ocean.util.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy klasy {@link pl.simulation.ocean.model.Fish}.
 * Obejmują stan początkowy, gospodarkę energią (utrata, zysk, koszt ruchu),
 * warunek śmierci oraz format tekstowy.
 */
class FishTest {

    /** Stałe klasy (zasięg detekcji, energia z planktonu) muszą odpowiadać specyfikacji. */
    @Test
    void exposesConstantsFromSpec() {
        assertEquals(5.0, Fish.DETECTION_RANGE);
        assertEquals(10, Fish.PLANKTON_ENERGY);
    }

    /** Nowa rybka startuje z energią 100, jest żywa i znajduje się na podanej pozycji. */
    @Test
    void startsAliveWithFullEnergyAtGivenPosition() {
        Fish fish = new Fish("Rybka1", 5, 7);

        assertEquals("Rybka1", fish.getName());
        assertEquals(100, fish.getEnergy());
        assertTrue(fish.isAlive());
        assertEquals(new Position(5, 7), fish.getPosition());
    }

    /** Po utracie całej energii rybka jest martwa, a jej energia wynosi 0. */
    @Test
    void diesWhenEnergyReachesZeroOrBelow() {
        Fish fish = new Fish("Rybka1", 0, 0);
        fish.loseEnergy(100);
        assertFalse(fish.isAlive());
        assertEquals(0, fish.getEnergy());
    }

    /** Operacje {@code loseEnergy} i {@code gainEnergy} zmieniają energię zgodnie z oczekiwaniem. */
    @Test
    void gainAndLoseEnergyAdjustValue() {
        Fish fish = new Fish("Rybka1", 1, 1);
        fish.loseEnergy(30);
        fish.gainEnergy(15);
        assertEquals(85, fish.getEnergy());
    }

    /** Jeden ruch pochłania dokładnie 5 jednostek energii i zwraca {@code true} gdy rybka żyje. */
    @Test
    void consumeMoveEnergyCostsFiveWhileAlive() {
        Fish fish = new Fish("Rybka1", 2, 2);
        assertTrue(fish.consumeMoveEnergy());
        assertEquals(95, fish.getEnergy());
    }

    /** Martwa rybka nie traci energii przy próbie ruchu — metoda zwraca {@code false}. */
    @Test
    void consumeMoveEnergyDoesNothingWhenDead() {
        Fish fish = new Fish("Rybka1", 2, 2);
        fish.loseEnergy(100);
        assertFalse(fish.consumeMoveEnergy());
        assertEquals(0, fish.getEnergy());
    }

    /** Setter pozycji zmienia położenie rybki na planszy. */
    @Test
    void setPositionUpdatesLocation() {
        Fish fish = new Fish("Rybka1", 0, 0);
        fish.setPosition(new Position(9, 9));
        assertEquals(new Position(9, 9), fish.getPosition());
    }

    /** Format tekstowy rybki zawiera imię, pozycję i energię w postaci {@code Imię@(x, y)[energia=N]}. */
    @Test
    void toStringIncludesNameEnergyAndPosition() {
        Fish fish = new Fish("Ala", 3, 4);
        assertEquals("Ala@(3, 4)[energia=100]", fish.toString());
    }
}
