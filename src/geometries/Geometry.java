package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * {@code Geometry} is an abstract class representing a geometric shape or object.
 * It serves as the foundation for defining geometric entities and enforces
 * the implementation of a method to compute normal vectors at given points.
 */
public abstract class Geometry {

    /**
     * Computes and returns the normal vector at a given point on the geometric object.
     * The normal vector is typically a unit vector perpendicular to the surface at the given point.
     *
     * @param point the point on the geometry at which to compute the normal
     * @return the normal vector at the specified point
     */
    public abstract Vector getNormal(Point point);
}
