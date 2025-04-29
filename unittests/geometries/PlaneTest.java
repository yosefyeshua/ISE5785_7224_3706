package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;


import java.util.List;

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

    /** Test method for {@link geometries.Plane#findIntersections(Ray)}. */
    @Test
    void findIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects plane (nor orthogonal or parallel)
        Plane p = new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(1, 1, 0));
        Ray ray = new Ray(new Point(0, 0, -1), new Vector(2, 2, 1));
        List<Point> intersections = p.findIntersections(ray);

        assertNotNull(intersections);
        assertEquals(List.of(new Point(2, 2, 0)), intersections, "Ray must intersect plane exactly once");

        // TC02: Ray does not intersect plane
        ray = new Ray(new Point(0, 0, 1), new Vector(2, 2, 1));

        assertNull(p.findIntersections(ray), "Ray must not intersect plane");

        // ============ Boundary Values Tests ==============
        // TC03: Ray is parallel to the plane (included in it)
        ray = new Ray(new Point(0, -1, 0), new Vector(1, 0, 0));

        assertNull(p.findIntersections(ray), "Should not return intersections for ray included in plane and parallel to it");

        // TC04: Ray is parallel to the plane (not included in it)
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));

        assertNull(p.findIntersections(ray), "Should not return intersections for ray parallel to plane");

        // TC05: Ray is orthogonal to plane and the head of the ray is in the plane
        ray = new Ray(new Point(2, 2, 0), new Vector(0, 0, 1));

        assertNull(p.findIntersections(ray), "Should not return intersections for ray orthogonal to plane and head of ray in plane");

        // TC06: Ray is orthogonal to plane and the head of the ray is under the plane pointed towards the plane
        ray = new Ray(new Point(2, 2, -1), new Vector(0, 0, 1));
        intersections = p.findIntersections(ray);

        assertNotNull(intersections);
        assertEquals(List.of(new Point(2, 2, 0)), intersections, "Ray must intersect plane exactly once");

        // TC07: Ray is orthogonal to plane and the head of the ray is above the plane pointed away from the plane
        ray = new Ray(new Point(2, 2, 1), new Vector(0, 0, 1));

        assertNull(p.findIntersections(ray), "Should not return intersections for ray orthogonal to plane and head of ray above plane pointed away from the plane");
    }
}