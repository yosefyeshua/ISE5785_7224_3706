package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * The {@code Cylinder} class represents a finite Tube in three-dimensional space.
 * It extends {@link Tube} by adding a height property, defining a bounded cylindrical shape.
 */
public class Cylinder extends Tube {

    /** The height of the cylinder, determining its length along the central axis. */
    private final double height;

    /**
     * Constructs a {@code Cylinder} with the specified radius, central axis, and height.
     *
     * @param radius the radius of the cylinder's base; must be positive
     * @param axis the central axis of the cylinder represented as a {@link Ray}
     * @param height the height of the cylinder; must be positive
     * @throws IllegalArgumentException if {@code height} is negative
     */
    protected Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be greater than zero");
        }
        this.height = height;
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

    /**
     * Returns the normal vector of the cylinder at a given point.
     *
     * @return the unit normal vector to the cylinder at a given point
     */
    @Override
    public Vector getNormal(Point point) {
        if (point.equals(this.axis.getHead())) {
            return this.axis.getDirection().scale(-1);
        }
        Vector u = point.subtract(this.axis.getHead());
        double t = this.axis.getDirection().dotProduct(u);

        if (Util.isZero(t))
            return this.axis.getDirection().scale(-1);
        else if (Util.isZero(t - height)) {
            return this.axis.getDirection();
        }
        else {
            return super.getNormal(point);
        }
    }
}

