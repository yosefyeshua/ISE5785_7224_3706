package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

public class Circle extends RadialGeometry{
    /**
     * The center point of the circle.
     */
    private final Point center;

    /**
     * The plane in which the circle lies, defined by its center and a normal vector.
     */
    private final Plane plane;

    /**
     * Constructs a {@code Circle} with a specified radius, center point, and normal vector.
     *
     * @param radius the radius of the circle
     * @param center the center point of the circle
     * @param normal the normal vector to the plane of the circle
     * @throws IllegalArgumentException if center or normal is null
     */
    protected Circle(double radius, Point center, Vector normal) {
        super(radius);
        if (center == null || normal == null) {
            throw new IllegalArgumentException("Center and normal cannot be null");
        }
        this.center = center;
        this.plane = new Plane(center, normal);
    }


    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(point);
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = plane.calculateIntersections(ray, maxDistance);
        if (intersections == null || intersections.isEmpty()) {
            return null; // No intersection with the plane
        }

        return intersections.getFirst().point.distance(center) < radius ?
                List.of(new Intersection(this, intersections.getFirst().point)) : null;
    }
}
