package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

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
        // ensure the result is orthogonal to all the edges

    }

}