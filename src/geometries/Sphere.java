package geometries;

import primitives.Point;
import primitives.Vector;

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

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(this.center).normalize();
    }
}

