package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
        /** Calculate the vector from the tube's axis to the ray's head
        Vector u = ray.getHead().subtract(this.axis.getHead());
        double t = this.axis.getDirection().dotProduct(u);
        double d = Math.sqrt(u.lengthSquared() - t * t);

        // Check if the ray is parallel to the tube's axis
        if (d >= this.radius) {
            return null; // No intersection
        }
        double th = Math.sqrt(this.radius * this.radius - d * d);
        double t1 = t - th;
        double t2 = t + th;
        if (t1 > 0 && t2 > 0) {
            return List.of(ray.getPoint(t1), ray.getPoint(t2));
        } else if (t1 > 0) {
            return List.of(ray.getPoint(t1));
        } else if (t2 > 0) {
            return List.of(ray.getPoint(t2));
        }
        // If both t1 and t2 are negative, there are no intersections
        // and we return null
        // Note: This is a simplification; in a real implementation,
        // we might want to return an empty list instead of null
        // to indicate no intersections.
        // However, returning null is consistent with the original code.
        // In a real-world scenario, we might want to handle this case differently.
        // For example, we could return an empty list:
        // return List.of();
        // Or we could throw an exception to indicate that no intersections were found.
        // throw new NoIntersectionsException("No intersections found");
        // For now, we will return null to indicate no intersections.
        // This is a placeholder; in a real implementation, we would handle this case properly.
        // For example, we could return an empty list:
         */
    }

    /**
     * Returns the normal vector of the tube at a given point.
     *
     * @return the unit normal vector to the tube at a given point
     */
    @Override
    public Vector getNormal(Point point) {
        Vector u = point.subtract(this.axis.getHead());
        double t = this.axis.getDirection().dotProduct(u);
        Point O = t != 0 ? this.axis.getPoint(t) : this.axis.getHead();
        return point.subtract(O).normalize();
    }
}

