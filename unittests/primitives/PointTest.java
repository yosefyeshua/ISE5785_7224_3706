package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Point} class.
 * Includes tests for subtract, add, distanceSquared, and distance methods.
 * Covers Equivalence Partitions (EP) and Boundary Value Analysis (BVA).
 */
class PointTest {

    private static final double DELTA = 0.000001;

    /**
     * Tests {@link primitives.Point#subtract(Point)}.
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Subtracting two general points
        Point p1 = new Point(3, 4, 5);
        Point p2 = new Point(2, 1, 3);
        Vector expected = new Vector(1, 3, 2);
        assertEquals(expected, p1.subtract(p2), "TC01: Subtraction should return (1, 3, 2)");

        // =============== Boundary Values Tests ==================

        // TC02: Subtracting a point from itself
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "TC02: Subtracting same point should return zero vector");
    }

    /**
     * Tests {@link primitives.Point#add(Vector)}.
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Adding a general vector to a point
        Point p = new Point(3, 4, 5);
        Vector v = new Vector(-2, -1, -3);
        Point expected = new Point(1, 3, 2);
        assertEquals(expected, p.add(v), "TC01: Point + vector should return (1, 3, 2)");

        // =============== Boundary Values Tests ==================

        // TC02: Adding a vector that brings point to origin
        p = new Point(2, 1, 3);
        assertEquals(Point.ZERO, p.add(v), "TC02: Should return Point.ZERO");
    }

    /**
     * Tests {@link primitives.Point#distanceSquared(Point)}.
     */
    @Test
    void distanceSquared() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Squared distance between general points
        Point p1 = new Point(3, 4, 5);
        Point p2 = new Point(2, 2, 3);
        assertEquals(9, p1.distanceSquared(p2), DELTA, "TC01: distanceSquared should return 9");

        // =============== Boundary Values Tests ==================

        // TC02: Squared distance from a point to itself
        assertEquals(0, p1.distanceSquared(p1), DELTA, "TC02: distanceSquared to self should be 0");
    }

    /**
     * Tests {@link primitives.Point#distance(Point)}.
     */
    @Test
    void distance() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Distance between general points
        Point p1 = new Point(3, 4, 5);
        Point p2 = new Point(2, 2, 3);
        assertEquals(3, p1.distance(p2), DELTA, "TC01: distance should return 3");

        // =============== Boundary Values Tests ==================

        // TC02: Distance from a point to itself
        assertEquals(0, p1.distance(p1), DELTA, "TC02: distance to self should be 0");
    }
}
