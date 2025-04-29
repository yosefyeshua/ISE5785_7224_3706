package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    /**
     * Tests {@link geometries.Tube#getNormal(Point)}.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test simple normal of Tube
        Tube t = new Tube(1, new Ray(Point.ZERO, new Vector(1, 0, 0)));

        assertEquals(new Vector(0, 0, 1), t.getNormal(new Point(1, 0, 1)), "Normal to tube is incorrect");

        // =============== Boundary Values Tests ==================
        // TC02: Test P-P0 is orthogonal to the axis
        assertEquals(new Vector(0, 0, 1), t.getNormal(new Point(0, 0, 1)), "Normal to tube is incorrect");
    }

    @Test
    void findIntersections() {
    }
}