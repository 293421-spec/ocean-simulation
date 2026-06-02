package pl.simulation.ocean.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link Position}.
 * Sprawdzają konstruktory, settery, obliczanie odległości euklidesowej
 * oraz poprawność metod {@code equals}, {@code hashCode} i {@code toString}.
 */
class PositionTest {

    /** Weryfikuje, że konstruktor z dwoma argumentami poprawnie zapisuje współrzędne. */
    @Test
    void constructorSetsCoordinates() {
        Position p = new Position(7, 12);
        assertEquals(7, p.getX());
        assertEquals(12, p.getY());
    }

    /** Sprawdza, że konstruktor kopiujący tworzy niezależny obiekt o tych samych współrzędnych. */
    @Test
    void copyConstructorDuplicatesCoordinates() {
        Position original = new Position(3, 4);
        Position copy = new Position(original);
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    /** Potwierdza, że settery X i Y aktualizują współrzędne. */
    @Test
    void settersUpdateCoordinates() {
        Position p = new Position(0, 0);
        p.setX(9);
        p.setY(1);
        assertEquals(9, p.getX());
        assertEquals(1, p.getY());
    }

    /** Odległość punktu do samego siebie powinna wynosić 0. */
    @Test
    void distanceToSamePositionIsZero() {
        Position p = new Position(5, 5);
        assertEquals(0.0, p.distanceTo(p), 0.001);
    }

    /**
     * Parametryzowany test sprawdzający poprawność odległości euklidesowej
     * dla kilku par punktów, w tym klasycznego trójkąta 3-4-5.
     */
    @ParameterizedTest
    @CsvSource({
            "0, 0, 3, 4, 5.0",
            "0, 0, 0, 0, 0.0",
            "10, 10, 13, 14, 5.0",
            "1, 1, 4, 5, 5.0"
    })
    void euclideanDistance(int x1, int y1, int x2, int y2, double expected) {
        assertEquals(expected, new Position(x1, y1).distanceTo(new Position(x2, y2)), 0.001);
    }

    /** Dwa obiekty o tych samych współrzędnych są równe i mają ten sam hash; różne współrzędne dają nierówność. */
    @Test
    void equalsAndHashCodeDependOnCoordinates() {
        Position a = new Position(2, 3);
        Position b = new Position(2, 3);
        Position c = new Position(2, 4);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "not a position");
        assertEquals(a, a);
    }

    /** Sprawdza format tekstowy pozycji: {@code (x, y)}. */
    @Test
    void toStringFormatsCoordinates() {
        assertEquals("(4, 8)", new Position(4, 8).toString());
    }
}
