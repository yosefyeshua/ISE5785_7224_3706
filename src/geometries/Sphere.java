package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
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
     * Constructs a {@code Sphere} with a given radius and center.
     * A sphere is defined by a single point (its center) and a radius extending uniformly in all directions.
     *
     * @param radius the radius of the sphere; must be positive
     * @param center the center of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper (Ray ray, double maxDistance) {
        if (this.center.equals(ray.getHead()))
            return List.of(new Intersection(this, ray.getPoint(this.radius)));
        Vector u = this.center.subtract(ray.getHead());
        double tm = ray.getDirection().dotProduct(u);
        double d = Math.sqrt(u.lengthSquared() - tm * tm);
        if (d >= this.radius) {
            return null; // No intersection
        }
        double th = Math.sqrt(this.radius * this.radius - d * d);
        double t1 = tm - th;
        double t2 = tm + th;
        if (t1 > 0 && t2 > 0 && Util.alignZero(t1 - maxDistance) < 0 && Util.alignZero(t2 - maxDistance) < 0) {
            return List.of(new Intersection(this, ray.getPoint(t1)),new Intersection(this,  ray.getPoint(t2)));
        } else if (t1 > 0 && Util.alignZero(t1 - maxDistance) < 0) {
            return List.of(new Intersection(this, ray.getPoint(t1)));
        } else if (t2 > 0 && Util.alignZero(t2 - maxDistance) < 0) {
            return List.of(new Intersection(this, ray.getPoint(t2)));
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

