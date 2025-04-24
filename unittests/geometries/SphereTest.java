package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

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
}