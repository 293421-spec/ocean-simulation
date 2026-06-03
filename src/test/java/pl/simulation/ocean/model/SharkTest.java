package pl.simulation.ocean.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy klasy {@link pl.simulation.ocean.model.Shark}.
 * Weryfikują stałe rekina, stan początkowy, mechanizm ataku na rybkę
 * (transfer energii), warunek śmierci oraz format tekstowy.
 */
class SharkTest {

    /** Stałe rekina (zasięg, obrażenia, zysk z ataku i planktonu) muszą odpowiadać specyfikacji. */
    @Test
    void exposesConstantsFromSpec() {
        assertEquals(7.0, Shark.DETECTION_RANGE);
        assertEquals(20, Shark.ATTACK_DAMAGE);
        assertEquals(20, Shark.ATTACK_ENERGY_GAIN);
        assertEquals(10, Shark.PLANKTON_ENERGY);
    }

    /** Nowy rekin startuje z energią 100 i jest żywy. */
    @Test
    void startsAliveWithFullEnergy() {
        Shark shark = new Shark("Rekin1", 10, 11);
        assertEquals("Rekin1", shark.getName());
        assertEquals(100, shark.getEnergy());
        assertTrue(shark.isAlive());
    }

    /** Pojedynczy atak odbiera rybce 20 energii i dodaje rekinowi 20; obie jednostki pozostają żywe. */
    @Test
    void attackTransfersEnergyAccordingToRules() {
        Shark shark = new Shark("Rekin1", 5, 5);
        Fish fish = new Fish("Rybka1", 5, 5);

        shark.attack(fish);

        assertEquals(80, fish.getEnergy());
        assertEquals(120, shark.getEnergy());
        assertTrue(fish.isAlive());
        assertTrue(shark.isAlive());
    }

    /** Pięć ataków (5 × 20 = 100) całkowicie wyczerpuje energię rybki i ją zabija. */
    @Test
    void repeatedAttackCanKillFish() {
        Shark shark = new Shark("Rekin1", 0, 0);
        Fish fish = new Fish("Rybka1", 0, 0);

        for (int i = 0; i < 5; i++) {
            shark.attack(fish);
        }

        assertFalse(fish.isAlive());
        assertEquals(0, fish.getEnergy());
    }

    /** Rekin ginie po utracie całej energii. */
    @Test
    void diesWhenEnergyDepleted() {
        Shark shark = new Shark("Rekin1", 1, 1);
        shark.loseEnergy(100);
        assertFalse(shark.isAlive());
    }

    /** Format tekstowy rekina jest analogiczny do rybki: {@code Imię@(x, y)[energia=N]}. */
    @Test
    void toStringIncludesNameEnergyAndPosition() {
        Shark shark = new Shark("Max", 2, 3);
        assertEquals("Max@(2, 3)[energia=100]", shark.toString());
    }
}
