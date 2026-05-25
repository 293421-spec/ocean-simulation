package pl.simulation.ocean.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void planktonEntityToStringUsesClassNameAndPosition() {
        Plankton plankton = new Plankton(2, 5);
        assertTrue(plankton.toString().startsWith("Plankton@"));
        assertTrue(plankton.toString().contains("(2, 5)"));
    }

    @Test
    void fishInheritsEntityPositionAccess() {
        Fish fish = new Fish("Rybka1", 1, 2);
        fish.setPosition(fish.getPosition());
        assertEquals(1, fish.getPosition().getX());
        assertEquals(2, fish.getPosition().getY());
    }
}
