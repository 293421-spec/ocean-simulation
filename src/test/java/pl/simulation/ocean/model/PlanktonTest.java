package pl.simulation.ocean.model;

import org.junit.jupiter.api.Test;
import pl.simulation.ocean.util.Position;

import static org.junit.jupiter.api.Assertions.*;

class PlanktonTest {

    @Test
    void startsAliveAtPosition() {
        Plankton plankton = new Plankton(6, 8);
        assertTrue(plankton.isAlive());
        assertEquals(new Position(6, 8), plankton.getPosition());
    }

    @Test
    void eatMarksPlanktonAsDead() {
        Plankton plankton = new Plankton(3, 3);
        plankton.eat();
        assertFalse(plankton.isAlive());
    }

    @Test
    void toStringShowsPositionAndEatenState() {
        Plankton alive = new Plankton(1, 2);
        Plankton eaten = new Plankton(1, 2);
        eaten.eat();

        assertEquals("Plankton@(1, 2)", alive.toString());
        assertEquals("Plankton@(1, 2)[zjedzony]", eaten.toString());
    }
}
