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
    private final Plane plane;

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
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = plane.calculateIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return null; // No intersection with the plane
        }

        if( intersections.getFirst().point.distance(center) < radius ) {return null;}
        return null;
    }
}
