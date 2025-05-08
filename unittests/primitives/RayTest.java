package primitives;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    /** Test method for {@link primitives.Ray#getPoint(double)}. */
    @Test
    void getPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Distance is positive
        Ray ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        Point expected = new Point(5, 0, 1);

        assertEquals(expected, ray.getPoint(5), "Incorrect ray point");

        // TC02: Distance is negative
        assertNull(ray.getPoint(-1), "Negative distance should return null");


        // =============== Boundary Values Tests ==================
        // TC03: Distance is 0.0
        assertEquals(ray.getHead(), ray.getPoint(0), "Distance of 0 should return the head of the ray");
    }

    @Test
    void findClosestPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Point in the middle of list is the closest to ray head
        Ray ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));

        List<Point> points = new ArrayList<>();
        points.add(new Point(5, 0, 1));
        points.add(new Point(6, 0, 1));
        points.add(new Point(4, 0, 1));
        points.add(new Point(7, 0, 1));
        points.add(new Point(8, 0, 1));

        Point expected = new Point(4, 0, 1);

        assertEquals(expected, ray.findClosestPoint(points), "Incorrect closest ray point");

        // =============== Boundary Values Tests ==================
        // TC02: Empty list of points
        assertNull(ray.findClosestPoint(new ArrayList<Point>()));

        // TC03: First point in list is closest to ray head
        points = new ArrayList<>();
        points.add(new Point(4, 0, 1));
        points.add(new Point(5, 0, 1));
        points.add(new Point(6, 0, 1));
        points.add(new Point(7, 0, 1));
        points.add(new Point(8, 0, 1));

        assertEquals(expected, ray.findClosestPoint(points), "Incorrect closest ray point");

        // TC04: Last point in list is closest to ray head
        points = new ArrayList<>();
        points.add(new Point(5, 0, 1));
        points.add(new Point(6, 0, 1));
        points.add(new Point(7, 0, 1));
        points.add(new Point(8, 0, 1));
        points.add(new Point(4, 0, 1));

        assertEquals(expected, ray.findClosestPoint(points), "Incorrect closest ray point");
    }
}