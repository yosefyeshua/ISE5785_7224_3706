package primitives;

import org.junit.jupiter.api.Test;

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
}