package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * The {@code Tube} class represents an infinite cylindrical surface in three-dimensional space.
 * It extends {@link RadialGeometry} by defining a central axis around which the tube is symmetrically constructed.
 */
public class Tube extends RadialGeometry {

    /** The central axis of the tube, represented as a {@link Ray}. */
    protected final Ray axis;

    /**
     * Constructs a {@code Tube} with the specified radius and central axis.
     *
     * @param radius the radius of the tube; must be positive
     * @param axis the central axis of the tube, represented as a {@link Ray}
     */
    protected Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        Vector u = point.subtract(this.axis.getHead());
        double t = this.axis.getDirection().dotProduct(u);
        Point O = t != 0 ? this.axis.getHead().add(this.axis.getDirection().scale(t)) : this.axis.getHead();
        return point.subtract(O).normalize();
    }
}

