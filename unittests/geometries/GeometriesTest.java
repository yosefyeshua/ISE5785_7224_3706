package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    @Test
    void add() {
    }

    @Test
    void findIntersections() {
        Triangle T = new Triangle(new Point(0, 5, 1), new Point(5, 0, 1), new Point(0, 0, 1));
        Sphere S = new Sphere(2, new Point(1, 0, 0));
        Plane P = new Plane(new Point(0, 0, -7), new Vector(0, 0, 1));
        Geometries G = new Geometries(T, S, P);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Some (but not all) geometries are intersected
        Ray ray = new Ray(new Point(1, 1, 0), new Vector(0, 0, 1));

        assertNotNull(G.findIntersections(ray));
        assertEquals(2, G.findIntersections(ray).size());


        // =============== Boundary Values Tests ==================
        // TC02: Geometries list is empty
        ray = new Ray(new Point(1, 1, 1), new Vector(3, 3, 3));
        assertNull(new Geometries().findIntersections(ray));

        // TC03: No geometries are intersected
        ray = new Ray(new Point(10, 10, 10), new Vector(5, 3, 1));

        assertNull(G.findIntersections(ray));

        // TC04: Only one geometry is intersected
        ray = new Ray(new Point(10, 10, 0), new Vector(0, 0, -1));

        assertNotNull(G.findIntersections(ray));
        assertEquals(1, G.findIntersections(ray).size());

        // TC05: All geometries are intersected
        ray = new Ray(new Point(1, 1, -10), new Vector(0, 0, 1));

        assertNotNull(G.findIntersections(ray));
        assertEquals(4, G.findIntersections(ray).size());
    }
}