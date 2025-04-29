package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * The {@code Sphere} class represents a three-dimensional sphere in a Cartesian coordinate system.
 * It extends {@link RadialGeometry}, inheriting the radius property, and defines a sphere by its center point.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center of the sphere in 3D space.
     * This immutable point determines the sphere's position and is used for various geometric calculations.
     */
    private final Point center;

    /**
     * Constructs a {@code Sphere} with a given radius and center.
     * A sphere is defined by a single point (its center) and a radius extending uniformly in all directions.
     *
     * @param radius the radius of the sphere; must be positive
     * @param center the center of the sphere
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    /**
     * Finds the intersection point(s) between the given {@link Ray} and this sphere.
     * <p>
     * The method supports rays that start outside, inside, or at the center of the sphere.
     * It returns only intersection points that lie in the direction of the ray.
     *
     * @param ray the ray to intersect with the sphere
     * @return a list containing 1 or 2 intersection points (depending on the case), or {@code null} if there is no intersection
     */
    @Override
    public List<Point> findIntersections (Ray ray) {
        if (this.center.equals(ray.getHead()))
            return List.of(ray.getPoint(this.radius));
        Vector u = this.center.subtract(ray.getHead());
        double tm = ray.getDirection().dotProduct(u);
        double d = Math.sqrt(u.lengthSquared() - tm * tm);
        if (d >= this.radius) {
            return null; // No intersection
        }
        double th = Math.sqrt(this.radius * this.radius - d * d);
        double t1 = tm - th;
        double t2 = tm + th;
        if (t1 > 0 && t2 > 0) {
            return List.of(ray.getPoint(t1), ray.getPoint(t2));
        } else if (t1 > 0) {
            return List.of(ray.getPoint(t1));
        } else if (t2 > 0) {
            return List.of(ray.getPoint(t2));
        }
        return null;
    }

    /**
     * Returns the normal vector of the sphere at a given point.
     *
     * @return the unit normal vector to the sphere at a given point
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(this.center).normalize();
    }
}

