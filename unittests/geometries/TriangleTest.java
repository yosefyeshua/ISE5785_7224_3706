package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    private static final double DELTA = 0.000001;
    /** Test method for {@link geometries.Triangle#getNormal(primitives.Point)}. */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point a = new Point(0, 0, 1);
        Point b = new Point(1, 0, 0);
        Point c = new Point(0, 1, 0);
        Point[] pts =
                { a, b, c};
        Triangle tri = new Triangle(a,b,c);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> tri.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = tri.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i <= 2; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 2 : i - 1])), DELTA,
                    "triangle's normal is not orthogonal to one of the edges");
    }

    /** Test method for {@link geometries.Triangle#findIntersections(Ray)}. */
    @Test
    void findIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects inside the triangle
        Triangle T = new Triangle(new Point(5, 0, 1), new Point(0, 0, 1), new Point(0, 5, 1));
        Ray ray = new Ray(new Point(1, 1, 0), new Vector(0, 0, 1));
        List<Point> intersections = T.findIntersections(ray);

        assertNotNull(intersections, "Should return intersections");
        assertEquals(1, intersections.size(), "Ray must intersect plane exactly once");
        assertEquals(List.of(new Point(1, 1, 1)), intersections, "Ray triangle intersections is incorrect");

        // TC02: Ray is outside of triangle opposite a side
        ray = new Ray(new Point(-2, 3, 0), new Vector(0, 0, 1));
        intersections = T.findIntersections(ray);

        assertNull(intersections, "Should not return intersections");

        // TC03: Ray is outside of triangle opposite a vertex
        ray = new Ray(new Point(-1, -1, 0), new Vector(0, 0, 1));
        intersections = T.findIntersections(ray);

        assertNull(intersections, "Should not return intersections");


        // ============ Boundary Values Tests ==============
        // TC04: Ray intersects a side of the triangle
        ray = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        intersections = T.findIntersections(ray);

        assertNull(intersections, "Should not return intersections");

        // TC05: Ray intersects a vertex
        ray = new Ray(new Point(5, 0, 0), new Vector(0, 0, 1));
        intersections = T.findIntersections(ray);

        assertNull(intersections, "Should not return intersections");

        // TC06: Ray intersects a continuation of one of the sides
        ray = new Ray(new Point(-5, 0, 0), new Vector(0, 0, 1));
        intersections = T.findIntersections(ray);

        assertNull(intersections, "Should not return intersections");



    }
}