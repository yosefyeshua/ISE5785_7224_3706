package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
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
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();
        Point axisP0 = axis.getHead();
        Vector vAxis = axis.getDirection();

        Vector deltaP = null;
        if (!p0.equals(axisP0)) {
            deltaP = p0.subtract(axisP0);
        }

        double vDotAxis = v.dotProduct(vAxis);
        double a = v.dotProduct(v) - vDotAxis * vDotAxis;

        double b, c;
        if (deltaP == null) {
            b = 0;
            c = -radius * radius;
        } else {
            double dPV = deltaP.dotProduct(v);
            double dPVA = deltaP.dotProduct(vAxis);
            b = 2 * (dPV - vDotAxis * dPVA);
            c = deltaP.dotProduct(deltaP) - dPVA * dPVA - radius * radius;
        }

        if (Util.isZero(a)) return null; // הקרן מקבילה לציר

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) return null;

        double sqrtDisc = Math.sqrt(discriminant);
        double t1 = (-b - sqrtDisc) / (2 * a);
        double t2 = (-b + sqrtDisc) / (2 * a);

        List<Intersection> intersections = new ArrayList<>();
        if (t1 > 0) intersections.add(new Intersection(this, ray.getPoint(t1)));
        if (t2 > 0 && !Util.isZero(t2 - t1)) intersections.add(new Intersection(this, ray.getPoint(t2)));

        return intersections.isEmpty() ? null : intersections;
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

