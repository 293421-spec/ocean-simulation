package pl.simulation.ocean.model;

import org.junit.jupiter.api.Test;
import pl.simulation.ocean.util.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy klasy {@link pl.simulation.ocean.model.Plankton}.
 * Weryfikują stan początkowy, mechanizm zjedzenia (flaga {@code eaten})
 * oraz format tekstowy żywego i zjedzonego planktonu.
 */
class PlanktonTest {

    /** Nowo utworzony plankton jest żywy i znajduje się na podanej pozycji. */
    @Test
    void startsAliveAtPosition() {
        Plankton plankton = new Plankton(6, 8);
        assertTrue(plankton.isAlive());
        assertEquals(new Position(6, 8), plankton.getPosition());
    }

    /** Wywołanie {@code eat()} powoduje, że plankton przestaje być żywy. */
    @Test
    void eatMarksPlanktonAsDead() {
        Plankton plankton = new Plankton(3, 3);
        plankton.eat();
        assertFalse(plankton.isAlive());
    }

    /** Żywy plankton ma format {@code Plankton@(x, y)}, zjednony — dodatkowo przyrostek {@code [zjedzony]}. */
    @Test
    void toStringShowsPositionAndEatenState() {
        Plankton alive = new Plankton(1, 2);
        Plankton eaten = new Plankton(1, 2);
        eaten.eat();

        assertEquals("Plankton@(1, 2)", alive.toString());
        assertEquals("Plankton@(1, 2)[zjedzony]", eaten.toString());
    }
}
