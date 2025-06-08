package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test simple normal of Tube
        Circle c = new Circle(1, new Point(0, 0, 1), new Vector(0, 0, 1));

        assertEquals(new Vector(0, 0, 1), c.getNormal(new Point(0.5, 0.5, 1)), "Normal to tube is incorrect");
    }

    @Test
    void calculateIntersectionsHelper() {
        // ============ Equivalence Partitions Tests ==============
        Circle c = new Circle(1, new Point(0, 0, 1), new Vector(0, 0, 1));
        // TC01: Ray intersects inside the circle
        Ray r = new Ray(new Point(0.5, 0.5, 0), new Vector(0, 0, 1));

        List<Point> intersections = c.findIntersections(r);
        assertNotNull(intersections, "Expected intersections to be found");
        assertEquals(1, intersections.size(), "Expected one intersection point");
        assertEquals(new Point(0.5, 0.5, 1), intersections.getFirst(), "Intersection point is incorrect");

        // TC02: Ray goes outside the circle
        r = new Ray(new Point(2, 2, 0), new Vector(0, 0, 1));
        assertNull(c.findIntersections(r), "Expected no intersections");


        // =============== Boundary Values Tests ==================
        // TC03: Ray is tangent to the circle
        r = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
        assertNull(c.findIntersections(r), "Expected no intersections");

        // TC04: Ray goes through the center of the circle
        r = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        intersections = c.findIntersections(r);
        assertNotNull(intersections, "Expected intersections to be found");
        assertEquals(1, intersections.size(), "Expected one intersection point");
        assertEquals(new Point(0, 0, 1), intersections.getFirst(), "Intersection point is incorrect");
    }
}