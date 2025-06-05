package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Cylinder} class represents a finite Tube in three-dimensional space.
 * It extends {@link Tube} by adding a height property, defining a bounded cylindrical shape.
 */
public class Cylinder extends Tube {

    /** The height of the cylinder, determining its length along the central axis. */
    private final double height;

    /** The top and bottom caps of the cylinder, represented as {@link Circle} objects. */
    private final Circle topCap;
    private final Circle bottomCap;

    /**
     * Constructs a {@code Cylinder} with the specified radius, central axis, and height.
     *
     * @param radius the radius of the cylinder's base; must be positive
     * @param axis the central axis of the cylinder represented as a {@link Ray}
     * @param height the height of the cylinder; must be positive
     * @throws IllegalArgumentException if {@code height} is negative
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be greater than zero");
        }
        this.height = height;

        Vector dir = axis.getDirection().normalize();
        Point base = axis.getHead();
        Point top = base.add(dir.scale(height));

        this.topCap = new Circle(radius, top, dir);
        this.bottomCap = new Circle(radius, base, dir.scale(-1));
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> sideIntersections = super.calculateIntersectionsHelper(ray);
        List<Intersection> intersections = new ArrayList<>();

        Vector axisDirection = axis.getDirection();
        Point base = axis.getHead();

        if (sideIntersections != null) {
            for (Intersection intersection : sideIntersections) {
                Point intersectionPoint = intersection.point;
                double t = Util.alignZero(axisDirection.dotProduct(intersectionPoint.subtract(base)));

                if (t > 0 && t < height) {
                    intersections.add(intersection);
                }
            }
        }

        List<Intersection> topIntersections = topCap.calculateIntersections(ray);
        if (topIntersections != null) {
            Point p = topIntersections.getFirst().point;
            intersections.add(new Intersection(this, p));
        }
        List<Intersection> bottomIntersections = bottomCap.calculateIntersections(ray);
        if (bottomIntersections != null) {
            Point p = bottomIntersections.getFirst().point;
            intersections.add(new Intersection(this, p));        }

        return intersections.isEmpty() ? null : intersections;
    }

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

