package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The {@code Plane} class represents a plane in three-dimensional space.
 * A plane is defined by a point lying on it and a normal vector perpendicular to it.
 * This class provides methods to construct a plane either from a point and a normal vector
 * or from three points in space that define a unique plane.
 */
public class Plane extends Geometry {

    /**
     * A point on the plane in three-dimensional space.
     * This point serves as a reference for defining the plane along with the normal vector.
     * It is immutable after construction.
     */
    private final Point q;

    /**
     * The normal vector of the plane.
     * The normal vector is a unit vector (magnitude 1) that is perpendicular to the plane.
     * It determines the orientation of the plane in space.
     * The vector is immutable and will be normalized upon construction.
     */
    private final Vector normal;

    /**
     * Constructs a {@code Plane} object using a point on the plane and a normal vector.
     * The provided normal vector is normalized upon construction to ensure it has a unit length.
     *
     * @param q the reference point on the plane
     * @param normal the vector perpendicular to the plane, which will be normalized
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Constructs a {@code Plane} object defined by three points in three-dimensional space.
     * The plane is determined using the first point as a reference and computing the normal vector
     * as the cross product of two vectors formed by the given points.
     *
     * @param p1 the first point lying on the plane
     * @param p2 the second point lying on the plane
     * @param p3 the third point lying on the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.q = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Returns the normal vector of the plane.
     *
     * @return the unit normal vector perpendicular to the plane
     */
    public Vector getNormal() {
        return this.normal;
    }

    @Override
    public Vector getNormal(Point point) {
        return this.normal;
    }
}

