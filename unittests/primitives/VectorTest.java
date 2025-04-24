package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Vector} class.
 * Includes tests for add, scale, dotProduct, crossProduct, lengthSquared, length, and normalize.
 * Covers Equivalence Partitions (EP) and Boundary Value Analysis (BVA).
 */
class VectorTest {

    private static final double DELTA = 0.00001;


    /**
     * Tests {@link primitives.Vector#Vector(double, double, double)}.
     */
    @Test
    void constructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test first constructor - basic
        assertDoesNotThrow(() -> new Vector(1,2,2), "Could not construct vector with Euclidean coordinates as parameters");

        // TC02: Test second constructor - creates Vector using a Double3
        assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)), "Could not construct vector with Double3 as parameter");

        // =============== Boundary Values Tests ==================
        // TC03: Test creation of zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "Created zero vector");

        // TC04: Test creation of zero vector with the second constructor
        assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO), "Created  Double3 zero vector");
    }



    /**
     * Tests {@link primitives.Vector#add(Vector)}.
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Adding two general vectors
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        Vector expected = new Vector(5, 7, 9);
        assertEquals(expected, v1.add(v2), "TC01: v1 + v2 should return (5, 7, 9)");

        // =============== Boundary Values Tests ==================

        // TC02: Adding a vector and its negative (should throw)
        Vector v3 = new Vector(-1, -2, -3);
        assertThrows(IllegalArgumentException.class, () -> v1.add(v3), "TC02: Adding opposite vectors should throw");
    }

    /**
     * Tests {@link primitives.Vector#scale(double)}.
     */
    @Test
    void scale() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Scaling vector by a positive scalar
        Vector v = new Vector(1, 2, 3);
        Vector expected = new Vector(2, 4, 6);
        assertEquals(expected, v.scale(2), "TC01: Scaling by 2 should return (2, 4, 6)");


        // TC02: Scaling vector by a negative scalar
        Vector v2 = new Vector(1, 2, 3);
        Vector expected2 = new Vector(-2, -4, -6);
        assertEquals(expected2, v2.scale(-2), "TC01: Scaling by -2 should return (-2, -4, -6)");

        // =============== Boundary Values Tests ==================

        // TC02: Scaling by 0 (should throw)
        assertThrows(IllegalArgumentException.class, () -> v.scale(0), "TC02: Scaling by 0 should throw");
    }

    /**
     * Tests {@link primitives.Vector#dotProduct(Vector)}.
     */
    @Test
    void dotProduct() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: General dot product
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        assertEquals(32, v1.dotProduct(v2), DELTA, "TC01: Dot product should be 32");

        // =============== Boundary Values Tests ==================

        // TC02: Dot product of orthogonal vectors
        Vector x = new Vector(1, 0, 0);
        Vector y = new Vector(0, 1, 0);
        assertEquals(0, x.dotProduct(y), DELTA, "TC02: Dot product of orthogonal vectors should be 0");
    }

    /**
     * Tests {@link primitives.Vector#crossProduct(Vector)}.
     */
    @Test
    void crossProduct() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: General cross product
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        Vector expected = new Vector(-3, 6, -3);
        assertEquals(expected, v1.crossProduct(v2), "TC01: Cross product should return (-3, 6, -3)");

        // =============== Boundary Values Tests ==================

        // TC02: Unit X × Unit Y = Unit Z
        Vector x = new Vector(1, 0, 0);
        Vector y = new Vector(0, 1, 0);
        assertEquals(new Vector(0, 0, 1), x.crossProduct(y), "TC02: X × Y should be Z");

        // TC03: Cross product of parallel vectors (should throw)
        Vector x2 = new Vector(2, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> x.crossProduct(x2), "TC03: Cross product of parallel vectors should throw");

        // TC04: Cross product of same vector with itself (should throw)
        assertThrows(IllegalArgumentException.class, () -> x.crossProduct(x), "TC04: Cross product of vector with itself should throw");
    }

    /**
     * Tests {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void lengthSquared() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: General vector length squared
        Vector v = new Vector(6, 6, 3);
        assertEquals(81, v.lengthSquared(), DELTA, "TC01: lengthSquared should return 81");

        // =============== Boundary Values Tests ==================

        // TC02: Unit vector
        v = new Vector(1, 0, 0);
        assertEquals(1, v.lengthSquared(), DELTA, "TC02: lengthSquared of unit vector should be 1");
    }

    /**
     * Tests {@link primitives.Vector#length()}.
     */
    @Test
    void length() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: General vector length
        Vector v = new Vector(6, 6, 3);
        assertEquals(9, v.length(), DELTA, "TC01: length should return 9");

        // =============== Boundary Values Tests ==================

        // TC02: Unit vector
        v = new Vector(1, 0, 0);
        assertEquals(1, v.length(), DELTA, "TC02: length of unit vector should be 1");
    }

    /**
     * Tests {@link primitives.Vector#normalize()}.
     */
    @Test
    void normalize() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Normalize a vector along the X-axis
        Vector v = new Vector(8, 0, 0);
        assertEquals(new Vector(1, 0, 0), v.normalize(), "TC01: Normalized vector should be unit X");

        // =============== Boundary Values Tests ==================

        // TC02: Normalize a unit vector (should return itself)
        v = new Vector(0, 1, 0);
        assertEquals(v, v.normalize(), "TC02: Normalizing a unit vector should return same vector");

    }

    /**
     * Tests {@link primitives.Vector#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Subtract two points
        Point p1 = new Point(1, 2, 3);
        assertEquals(new Vector(1, 2, 3), new Point(2, 4, 6).subtract(p1), "TC01: (2,4,6) - p1 should return (1, 2, 3)");

        // =============== Boundary Values Tests ==================

        // TC02: Subtract equal points
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "TC02: Subtracting same point should throw");
    }
}
