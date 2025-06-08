package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    /**
     * Tests {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        Vector n = new Vector(1,0,0);
        Cylinder c = new Cylinder(1, new Ray(Point.ZERO, n),1);

        // TC01: the point is on the top base surface (not on the edge)
        assertEquals(new Vector(0, 0, 1),
                c.getNormal(new Point(0.5, 0, 1)),
                "Bad normal for point on top base surface of cylinder");

        // TC02: the point is on the bottom base surface (not on the edge)
        assertEquals(n.scale(-1), c.getNormal(new Point(0, 0.5, 0)),
                "Bad normal for point on bottom base surface of cylinder");

        // TC03: the point is on the lateral surface of the cylinder
        assertEquals(n, c.getNormal(new Point(1, 0.5, 0)),
                "Bad normal for point on side surface of cylinder");

        // =============== Boundary Values Tests ==================
        // TC04: the point is exactly in the center of the bottom base
        assertEquals(n.scale(-1), c.getNormal(Point.ZERO),
                "Bad normal for point at center of bottom base");

        // TC05: the point is on the bottom edge between base and lateral surface
        assertEquals(n, c.getNormal(new Point(1, 0, 0)),
                "Bad normal for point on bottom edge");

        // TC06: the point is at the center of the top base
        assertEquals(n.scale(-1), c.getNormal(new Point(0, 0, 1)),
                "Bad normal for point at center of top base");

        // TC07: the point is on the top edge between base and lateral surface
        assertEquals(n, c.getNormal(new Point(1, 0, 1)),
                "Bad normal for point on top edge");


    }

    /**
     * Tests {@link geometries.Cylinder#calculateIntersections(Ray)}.
     */
    @Test
    void findIntersections() {
        Cylinder cylinder = new Cylinder(2, new Ray(Point.ZERO, new Vector(0, 0, 1)), 4);
        Point capCenterTop = new Point(0, 0, 4);
        Point capCenterBottom = new Point(0, 0, 0);
        Vector axisDir = new Vector(0, 0, 1);

        // ============ Equivalence Partition Tests ==============

        // TC01: Ray intersects through the side
        Ray ray = new Ray(new Point(0, -3, 1), new Vector(0, 1, 0));
        assertNotNull(cylinder.findIntersections(ray), "TC01: Ray intersects through the side");
        assertEquals(2, cylinder.findIntersections(ray).size(),
                "expected 2 intersections");
        assertEquals(
                List.of(new Point(0, -2, 1), new Point(0, 2, 1)),
                cylinder.findIntersections(ray),
                "TC01: Ray intersects through the side"
        );

        // TC02: Ray starts inside and exits through lateral surface
        ray = new Ray(new Point(1, 1, 2), new Vector(0.3, 0.1, 1));
        assertNotNull(cylinder.findIntersections(ray), "TC02: Ray starts inside and exits through lateral surface");
        assertEquals(1, cylinder.findIntersections(ray).size(),
                "expected 1 intersections");
        assertEquals(
                List.of(new Point(1.6, 1.2, 4)),
                cylinder.findIntersections(ray),
                "TC02: Ray starts inside and exits through lateral surface"
        );

        // TC03: Ray intersects both caps (along axis)
        ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        assertNotNull(cylinder.findIntersections(ray), "TC03: Ray intersects both caps (along axis)");
        assertEquals(2, cylinder.findIntersections(ray).size(),
                "expected 2 intersections");
        assertEquals(
                List.of(new Point(0, 0, 4), new Point(0, 0, 0)),
                cylinder.findIntersections(ray),
                "TC03: Ray intersects both caps (along axis)"
        );

        // TC04: Ray enters bottom cap, exits lateral
        ray = new Ray(new Point(0, 0, -1), new Vector(0, 1, 1));

        assertNotNull(cylinder.findIntersections(ray), "TC04: Ray enters bottom cap, exits lateral");
        assertEquals(2, cylinder.findIntersections(ray).size(),
                "expected 2 intersections");
        assertEquals(
                List.of(new Point(0, 2, 1), new Point(0, 1, 0)),
                cylinder.findIntersections(ray),
                "TC04: Ray enters bottom cap, exits lateral"
        );

        // TC05: Ray enters lateral, exits top cap
        ray = new Ray(new Point(0, 3, 1), new Vector(0, -1, 1));
        assertNotNull(cylinder.findIntersections(ray), "TC05: Ray enters lateral, exits top cap");
        assertEquals(2, cylinder.findIntersections(ray).size(),
                "expected 2 intersections");
        assertEquals(
                List.of(new Point(0, 2, 2), new Point(0, 0, 4)),
                cylinder.findIntersections(ray),
                "TC05: Ray enters lateral, exits top cap"
        );

        // =============== Boundary Value Tests ==================

        // TC06: Ray is tangent to side surface
        ray = new Ray(new Point(2, -1, 2), new Vector(0, 1, 0));
        assertNull(
                cylinder.findIntersections(ray),
                "TC06: Ray is tangent to side surface"
        );

        // TC07: Ray starts on surface and exits
        ray = new Ray(new Point(2, 0, 2), new Vector(-1, 0, 0));
        assertNotNull(cylinder.findIntersections(ray), "TC07: Ray starts on surface and exits");
        assertEquals(1, cylinder.findIntersections(ray).size(),
                "expected 1 intersections");
        assertEquals(
                List.of(new Point(-2, 0, 2)),
                cylinder.findIntersections(ray),
                "TC07: Ray starts on surface and exits"
        );

        // TC08: Ray starts at top cap center
        ray = new Ray(capCenterTop, new Vector(0, 0, -1));
        assertNotNull(cylinder.findIntersections(ray), "TC08: Ray starts at top cap center");
        assertEquals(1, cylinder.findIntersections(ray).size(),
                "expected 1 intersections");
        assertEquals(
                List.of(new Point(0, 0, 0)),
                cylinder.findIntersections(ray),
                "TC08: Ray starts at top cap center"
        );

        // TC09: Ray tangent to cap at edge
        ray = new Ray(new Point(2, 0, 4), new Vector(0, 1, 0));
        assertNull(cylinder.findIntersections(ray), "TC09: Ray tangent to cap at edge");

        // TC10: Ray along edge (no valid intersection)
        ray = new Ray(new Point(2, 0, 4), new Vector(0, 0, -1));
        assertNull(cylinder.findIntersections(ray), "TC10: Ray along edge (no valid intersection)");

        // TC11: Ray inside cylinder parallel to axis
        ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNotNull(cylinder.findIntersections(ray), "TC11: Ray inside cylinder parallel to axis");
        assertEquals(1, cylinder.findIntersections(ray).size(),
                "expected 1 intersections");
        assertEquals(
                List.of(new Point(0, 0, 4)),
                cylinder.findIntersections(ray),
                "TC11: Ray inside cylinder parallel to axis"
        );

        // TC12: Ray inside cylinder but goes sideways (misses walls)
        ray = new Ray(new Point(0, 0, 2), new Vector(0, -1, -1));
        assertNull(cylinder.findIntersections(ray), "TC12: Ray inside cylinder but goes sideways (misses walls)");
    }
}

