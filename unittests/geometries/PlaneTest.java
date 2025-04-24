package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {
    private static final double DELTA = 0.000001;
    /**
     * Tests {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void constructor() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1, 3, 5);
        Point p2 = new Point(5, 3, 1);
        Point p3 = new Point(7, 8, 9);
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        Plane P = new Plane(p1, p2, p3);

        // TC01: Test normal vector is orthogonal to at least two vectors between the points of the plane, and that it's length is 1
        Vector normal = P.getNormal();
        assertEquals(0, normal.dotProduct(v1), DELTA, "Normal should be orthogonal to v1");
        assertEquals(0, normal.dotProduct(v2), DELTA, "Normal should be orthogonal to v2");
        assertEquals(1, normal.length(), DELTA, "Normal length should be 1");

        // ============ Boundary Values Tests ==============

        // TC02: Test first point is not equal to second point
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p3), "Cannot create Plane with identical points");

        // TC03: Test second point is not equal to third point
        assertThrows(IllegalArgumentException.class, () -> new Plane(p3, p1, p1), "Cannot create Plane with identical points");

        // TC04: Test first point is not equal to third point
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p3, p1), "Cannot create Plane with identical points");

        // TC05: Test all points are not equal to each other
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1), "Cannot create Plane with identical points");

        // TC06: Test points are not collinear
        Point p4 = new Point(1, 2, 3);
        Point p5 = new Point(4, 5, 6);
        Point p6 = new Point(7, 8, 9);
        assertThrows(IllegalArgumentException.class, () -> new Plane(p4, p5, p6), "Cannot create Plane with collinear points");

    }

    /**
     * Tests {@link geometries.Plane#getNormal(Point) and for {@link geometries.Plane#getNormal()}}.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test normal of plane
        Plane p = new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(1, 1, 0));

        Vector result1 = p.getNormal();
        Vector result2 = p.getNormal(new Point(1, 0, 0));
        Vector expected1 = new Vector(0, 0, 1);
        Vector expected2 = new Vector(0, 0, -1);

        // Check that normals are equal to expected normal of plane which is (0, 0, 1)
        assertTrue(result1.equals(expected1) || result1.equals(expected2), "Normal should orthogonal to the plane");
        assertTrue(result2.equals(expected1) || result2.equals(expected2), "Normal should orthogonal to the plane");
    }
}