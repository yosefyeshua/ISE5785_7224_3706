package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    /**
     * Tests {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        Vector n = new Vector(1,0,0);
        Cylinder c = new Cylinder(1, new Ray(Point.ZERO, n),1);

        // TC01: the point is on the top base surface (not on the edge)
        assertEquals(new Vector(0, 0, 1),
                c.getNormal(new Point(0.5, 0, 1)),
                "Bad normal for point on top base surface of cylinder");

        // TC02: the point is on the bottom base surface (not on the edge)
        assertEquals(n.scale(-1), c.getNormal(new Point(0, 0.5, 0)),
                "Bad normal for point on bottom base surface of cylinder");

        // TC03: the point is on the lateral surface of the cylinder
        assertEquals(n, c.getNormal(new Point(1, 0.5, 0)),
                "Bad normal for point on side surface of cylinder");

        // =============== Boundary Values Tests ==================
        // TC04: the point is exactly in the center of the bottom base
        assertEquals(n.scale(-1), c.getNormal(Point.ZERO),
                "Bad normal for point at center of bottom base");

        // TC05: the point is on the bottom edge between base and lateral surface
        assertEquals(n, c.getNormal(new Point(1, 0, 0)),
                "Bad normal for point on bottom edge");

        // TC06: the point is at the center of the top base
        assertEquals(n.scale(-1), c.getNormal(new Point(0, 0, 1)),
                "Bad normal for point at center of top base");

        // TC07: the point is on the top edge between base and lateral surface
        assertEquals(n, c.getNormal(new Point(1, 0, 1)),
                "Bad normal for point on top edge");


    }

    @Test
    void findIntersections() {
    }
}