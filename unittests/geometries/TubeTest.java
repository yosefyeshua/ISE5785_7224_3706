package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    /**
     * Tests {@link geometries.Tube#getNormal(Point)}.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test simple normal of Tube
        Tube t = new Tube(1, new Ray(Point.ZERO, new Vector(1, 0, 0)));

        assertEquals(new Vector(0, 0, 1), t.getNormal(new Point(1, 0, 1)), "Normal to tube is incorrect");

        // =============== Boundary Values Tests ==================
        // TC02: Test P-P0 is orthogonal to the axis
        assertEquals(new Vector(0, 0, 1), t.getNormal(new Point(0, 0, 1)), "Normal to tube is incorrect");
    }

    @Test
    void findIntersections() {
        Tube t1 = new Tube(1, new Ray(new Point(0, 0, 1), new Vector(0, 0, 1)));
        Tube t2 = new Tube(2, new Ray(new Point(0, 0, 1), new Vector(0, 0, 1)));
        Tube ts2 = new Tube(Math.sqrt(2), new Ray(new Point(0, 0, 1), new Vector(0, 0, 1)));
        Tube t4 = new Tube(4, new Ray(new Point(0, 0, 1), new Vector(0, 0, 1)));
        Tube ts5 = new Tube(Math.sqrt(5), new Ray(new Point(0, 0, 1), new Vector(0, 0, 1)));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray is outside the tube (0 points)
        Ray r = new Ray(new Point(0, 5, 2), new Vector(1, 1, 0));
        assertNull(t1.findIntersections(r), "Ray's line out of tube");
        // TC02: Ray starts before and crosses the tube (2 points)
        r = new Ray(new Point(-5, -5, 2), new Vector(1, 1, 0));
        assertNotNull(ts2.findIntersections(r), "Ray's line out of tube");
        assertEquals(2, ts2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, -1, 2), new Point(1, 1, 2)), ts2.findIntersections(r),
                "Ray crosses tube");
        // TC03: Ray starts inside the tube (1 point)
        r = new Ray(new Point(0, 1, 2), new Vector(1, 1, 1));
        assertNotNull(ts5.findIntersections(r), "Ray's line out of tube");
        assertEquals(1, ts5.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 2, 3)), ts5.findIntersections(r), "Ray crosses tube");


        // =============== Boundary Values Tests ==================

        // group 1: ray starts in the center of the tube
        // TC11: Ray similar the tube's ray (0 points)
        r = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC16: Ray is the tube's ray * -1 (0 points)
        r = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC17: Ray start the center and cross the tube (1 point)
        r = new Ray(new Point(0, 0, 1), new Vector(1, 1, 1));
        assertNotNull(ts2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, ts2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 1, 2)), ts2.findIntersections(r), "Ray crosses tube");
        // TC18: Ray start the center and orthogonal the tube's ray (1 point)
        r = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        assertNotNull(t1.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t1.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 1)), t1.findIntersections(r), "Ray crosses tube");
        // group 2: ray starts on the tube's ray
        // TC21: Ray on the tube ray (0 point)
        r = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC22: Ray on the tube ray * -1 (0 point)
        r = new Ray(new Point(0, 0, 2), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC23: Ray on the tube ray and cross the tube (1 point)
        r = new Ray(new Point(0, 0, 2), new Vector(1, 1, 1));
        assertNotNull(ts2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, ts2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 1, 3)), ts2.findIntersections(r), "Ray crosses tube");
        // TC24: Ray on the tube ray and orthogonal the tube's ray (1 point)
        r = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
        assertNotNull(t2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(2, 0, 2)), t2.findIntersections(r), "Ray crosses tube");
        // group 3: ray starts on the tube's ray * -1
        // TC31: Ray's direction is the tube's ray's direction * -1 (0 point)
        r = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC32: Ray's direction is the tube's ray's direction (0 point)
        r = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC33: Ray's direction is orthogonal the tube's ray's direction (1 point)
        r = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        assertNotNull(t1.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t1.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 0)), t1.findIntersections(r), "Ray crosses tube");
        // TC34: Ray cross the tube (1 point)
        r = new Ray(new Point(0, 0, 0), new Vector(1, 1, 1));
        assertNotNull(ts2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, ts2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 1, 1)), ts2.findIntersections(r), "Ray crosses tube");
        // group 4: ray starts on base of the center in the tube
        // TC41: Ray trough the center of the base (1 point)
        r = new Ray(new Point(0, 1, 1), new Vector(0, -1, 0));
        assertNotNull(t2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, -2, 1)), t2.findIntersections(r), "Ray crosses tube");
        // TC42: Ray * -1 through the center of the base (1 point)
        r = new Ray(new Point(0, 1, 1), new Vector(0, 1, 0));
        assertNotNull(t2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 2, 1)), t2.findIntersections(r), "Ray crosses tube");
        // TC43: Ray's direction is the tube's ray's direction (0 point)
        r = new Ray(new Point(0, 1, 1), new Vector(0, 0, 1));
        assertNull(t2.findIntersections(r), "Ray's line does not cross tube");
        // TC44: Ray's direction is the tube's ray's direction * -1 (0 point)
        r = new Ray(new Point(0, 1, 1), new Vector(0, 0, -1));
        assertNull(t2.findIntersections(r), "Ray's line does not cross tube");
        // TC45: Ray cross the tube (1 point)
        r = new Ray(new Point(0, 1, 1), new Vector(1, 1, 1));
        assertNotNull(ts5.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, ts5.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 2, 2)), ts5.findIntersections(r), "Ray crosses tube");
        // group 5: ray starts on base of tube and on the edge of the tube
        // TC51: Ray trough the center of the base (1 point)
        r = new Ray(new Point(1, 0, 1), new Vector(-1, 0, 0));
        assertNotNull(t1.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t1.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, 0, 1)), t1.findIntersections(r), "Ray crosses tube");
        // TC52: Ray * -1 through the center of the base (0 point)
        r = new Ray(new Point(1, 0, 1), new Vector(1, 0, 0));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC53: Ray's direction is the tube's ray's direction (0 point)
        r = new Ray(new Point(1, 0, 1), new Vector(0, 0, 1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC54: Ray's direction is the tube's ray's direction * -1 (0 point)
        r = new Ray(new Point(1, 0, 1), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC55: Ray cross the tube (1 point)
        r = new Ray(new Point(2, 0, 2), new Vector(-1, 0, 1));
        assertNotNull(t2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(-2, 0, 6)), t2.findIntersections(r), "Ray crosses tube");
        // group 6: ray starts on the edge of the tube
        // TC61: Ray trough the center of the base (1 point)
        r = new Ray(new Point(1, 0, 2), new Vector(-1, 0, -1));
        assertNotNull(t1.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t1.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, 0, 0)), t1.findIntersections(r), "Ray crosses tube");
        // TC62: Ray * -1 through the center of the base (1 point)
        r = new Ray(new Point(1, 0, 2), new Vector(1, 0, 1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC63: Ray's direction is the tube's ray's direction (0 point)
        r = new Ray(new Point(1, 0, 2), new Vector(0, 0, 1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC64: Ray's direction is the tube's ray's direction * -1 (0 point)
        r = new Ray(new Point(1, 0, 2), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC65: Ray cross the tube (1 point)
        r = new Ray(new Point(1, 0, 2), new Vector(-1, 0, 1));
        assertNotNull(t1.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t1.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, 0, 4)), t1.findIntersections(r), "Ray crosses tube");
        // TC66: Ray * -1 cross the tube (0 point)
        r = new Ray(new Point(1, 0, 2), new Vector(1, 0, -1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC67: Ray orthogonal tube's ray (1 point)
        r = new Ray(new Point(1, 0, 2), new Vector(-1, 0, 0));
        assertNotNull(t1.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t1.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, 0, 2)), t1.findIntersections(r), "Ray crosses tube");
        // TC68: Ray * -1 orthogonal tube's ray (0 point)
        r = new Ray(new Point(1, 0, 2), new Vector(1, 0, 0));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // group 7: ray starts in the tube
        // TC71: Ray trough the center (1 point)
        r = new Ray(new Point(1, 0, 2), new Vector(-1, 0, -1));
        assertNotNull(t2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(-2, 0, -1)), t2.findIntersections(r), "Ray crosses tube");
        // TC72: Ray * -1 through the center (1 point)
        r = new Ray(new Point(1, 0, 2), new Vector(1, 0, 1));
        assertNotNull(t2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(2, 0, 3)), t2.findIntersections(r), "Ray crosses tube");
        // TC73: Ray's direction is the tube's ray's direction (0 point)
        r = new Ray(new Point(1, 0, 2), new Vector(0, 0, 1));
        assertNull(t4.findIntersections(r), "Ray's line does not cross tube");
        // TC74: Ray's direction is the tube's ray's direction * -1 (0 point)
        r = new Ray(new Point(1, 0, 2), new Vector(0, 0, -1));
        assertNull(t4.findIntersections(r), "Ray's line does not cross tube");
        // TC75: Ray orthogonal the tube (1 point)
        r = new Ray(new Point(1, 0, 2), new Vector(-1, 0, 0));
        assertNotNull(t4.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(1, t4.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(-4, 0, 2)), t4.findIntersections(r), "Ray crosses tube");
        // group 8: ray starts on base of tube and out of the edge of the tube
        // TC81: Ray trough the center of the base (2 points)
        r = new Ray(new Point(2, 0, 1), new Vector(-1, 0, 0));
        assertNotNull(t1.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(2, t1.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 1), new Point(-1, 0, 1)), t1.findIntersections(r),
                "Ray crosses tube");
        // TC82: Ray * -1 through the center of the base (0 point)
        r = new Ray(new Point(2, 0, 1), new Vector(1, 0, 0));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC83: Ray's direction is the tube's ray's direction (0 point)
        r = new Ray(new Point(2, 0, 1), new Vector(0, 0, 1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC84: Ray's direction is the tube's ray's direction * -1 (0 point)
        r = new Ray(new Point(2, 0, 1), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC85: Ray cross the tube (2 points)
        r = new Ray(new Point(2, 2, 1), new Vector(-1, -1, 1));
        assertNotNull(ts2.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(2, ts2.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 1, 2), new Point(-1, -1, 4)), ts2.findIntersections(r),
                "Ray crosses tube");
        // group 9: ray starts out of the tube
        // TC91: Ray trough the center of the base (2 point)
        r = new Ray(new Point(3, 0, 4), new Vector(-1, 0, -1));
        assertNotNull(t1.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(2, t1.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 2), new Point(-1, 0, 0)), t1.findIntersections(r),
                "Ray crosses tube");
        // TC92: Ray * -1 through the center of the base (0 point)
        r = new Ray(new Point(3, 0, 4), new Vector(1, 0, 1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC93: Ray's direction is the tube's ray's direction (0 point)
        r = new Ray(new Point(3, 0, 4), new Vector(0, 0, 1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC94: Ray's direction is the tube's ray's direction * -1 (0 point)
        r = new Ray(new Point(3, 0, 4), new Vector(0, 0, -1));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC95: Ray tangent the tube (0 points)
        r = new Ray(new Point(3, 1, 1), new Vector(-1, 0, 0));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC96: Ray tangent the tube (0 points)
        r = new Ray(new Point(3, 1, 2), new Vector(-1, 0, 0));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC97: Ray tangent the tube (0 points)
        r = new Ray(new Point(3, 1, -1), new Vector(-1, 0, 0));
        assertNull(t1.findIntersections(r), "Ray's line does not cross tube");
        // TC98: Ray orthogonal the tube's ray (2 points)
        r = new Ray(new Point(3, 0, -3), new Vector(-1, 0, 0));
        assertNotNull(t1.findIntersections(r), "Ray's line does not cross tube");
        assertEquals(2, t1.findIntersections(r).size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, -3), new Point(-1, 0, -3)), t1.findIntersections(r),
                "Ray crosses tube");
    }
}