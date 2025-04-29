package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

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
     * Finds all intersection points between the given {@link Ray} and this plane.
     * <p>
     * The method calculates the intersection point of the ray with the plane, if it exists,
     * and returns a list containing the intersection point. If the ray is parallel to the plane
     * or the intersection is behind the ray's origin, the method returns {@code null}.
     *
     * @param ray the ray to intersect with the plane
     * @return a list containing the intersection point, or {@code null} if there are none
     */
    @Override
    public List<Point> findIntersections (Ray ray) {
        Vector n = this.normal;
        Point Q = this.q;
        Point P0 = ray.getHead();
        Vector v = ray.getDirection();

        double nv = n.dotProduct(v);
        if (Util.isZero(nv)) {
            return null;
        }

        Vector QminusP0 = Q.subtract(P0);
        double nQminusP0 = n.dotProduct(QminusP0);
        double t = Util.alignZero(nQminusP0 / nv);

        if (t <= 0) {
            return null;
        }

        return List.of(ray.getPoint(t));
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

