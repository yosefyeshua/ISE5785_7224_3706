package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    /**Test method for {@link geometries.Sphere#getNormal(Point point)}. */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: a simple sphere
         Sphere s = new Sphere(1,new Point(0,0,0));
         assertEquals(new Vector(1,0,0), s.getNormal(new Point(1,0,0)),"Normal to sphere is incorrect");


    }


    /**Test method for {@link geometries.Sphere#findIntersections(Ray)}. */
    @Test
    void testFindIntersections() {
        Sphere s = new Sphere(Math.sqrt(2), new Point(1, 0, 0));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray is outside the sphere (0 points)
        Ray r = new Ray(new Point(1, 5, 0), new Vector(0, 1, 0));
        assertNull(s.findIntersections(r), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        r = new Ray(new Point(1, -5, 1), new Vector(0, 1, 0));
        List<Point> result = s.findIntersections(r);
        assertNotNull(result, "Can't be null");
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, -1, 1), new Point(1, 1, 1)), result, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        r = new Ray(new Point(1, 0, 1), new Vector(0, 1, 0));
        result = s.findIntersections(r);
        assertNotNull(result, "Can't be null");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 1, 1)), result, "Ray from inside the sphere");

        // TC04: Ray starts after the sphere (0 points)
        r = new Ray(new Point(1, 5, 1), new Vector(0, 1, 0));
        assertNull(s.findIntersections(r), "Ray after sphere");

        // =============== Boundary Values Tests ==================

        // **** Group 1: Ray's line crosses the sphere (but not through center)

        // TC11: Ray starts at sphere and goes inside (1 point)
        s = new Sphere(2, new Point(1, 0, 0));
        r = new Ray(new Point(1, 2, 0), new Vector(0, -1, 0));
        result = s.findIntersections(r);
        assertNotNull(result, "Can't be null");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, -2, 0)), result, "Ray from sphere to inside");

        // TC12: Ray starts at sphere and goes outside (0 points)
        r = new Ray(new Point(1, 2, 0), new Vector(0, 1, 0));
        assertNull(s.findIntersections(r), "Ray from sphere to outside");

        // **** Group 2: Ray's line goes through the center

        // TC21: Ray starts before the sphere (2 points)
        r = new Ray(new Point(1, 3, 0), new Vector(0, -1, 0));
        result = s.findIntersections(r);
        assertNotNull(result, "Can't be null");
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 2, 0), new Point(1, -2, 0)), result, "Ray crosses center");

        // TC22: Ray starts at sphere and goes inside (1 point)
        r = new Ray(new Point(1, 2, 0), new Vector(0, -1, 0));
        result = s.findIntersections(r);
        assertNotNull(result, "Can't be null");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, -2, 0)), result, "Ray from sphere to center");

        // TC23: Ray starts inside (1 point)
        r = new Ray(new Point(1, 1, 0), new Vector(0, -1, 0));
        result = s.findIntersections(r);
        assertNotNull(result, "Can't be null");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, -2, 0)), result, "Ray from inside");

        // TC24: Ray starts at the center (1 point)
        r = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
        result = s.findIntersections(r);
        assertNotNull(result, "Can't be null");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 2, 0)), result, "Ray from center");

        // TC25: Ray starts at sphere and goes outside (0 points)
        r = new Ray(new Point(1, 2, 0), new Vector(0, 1, 0));
        assertNull(s.findIntersections(r), "Ray from sphere outside center");

        // TC26: Ray starts after sphere (0 points)
        r = new Ray(new Point(1, 3, 0), new Vector(0, 1, 0));
        assertNull(s.findIntersections(r), "Ray after sphere center");

        // **** Group 3: Ray's line is tangent to the sphere

        // TC31: Ray starts before the tangent point
        r = new Ray(new Point(0, 2, 0), new Vector(1, 0, 0));
        assertNull(s.findIntersections(r), "Ray before tangent");

        // TC32: Ray starts at the tangent point
        r = new Ray(new Point(1, 2, 0), new Vector(1, 0, 0));
        assertNull(s.findIntersections(r), "Ray at tangent point");

        // TC33: Ray starts after the tangent point
        r = new Ray(new Point(2, 2, 0), new Vector(1, 0, 0));
        assertNull(s.findIntersections(r), "Ray after tangent point");

        // **** Group 4: Special cases

        // TC41: Ray's line is outside sphere, ray is orthogonal to start-center line
        r = new Ray(new Point(1, 3, 0), new Vector(1, 0, 0));
        assertNull(s.findIntersections(r), "Ray orthogonal outside");

        // TC42: Ray starts inside, ray is orthogonal to start-center line
        s = new Sphere(Math.sqrt(2), new Point(1, 0, 0));
        r = new Ray(new Point(1, 1, 0), new Vector(1, 0, 0));
        result = s.findIntersections(r);
        assertNotNull(result, "Can't be null");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2, 1, 0)), result, "Ray orthogonal inside");
    }

}