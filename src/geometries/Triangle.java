package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * The {@code Triangle} class represents a two-dimensional triangle in a 3D Cartesian coordinate system.
 * It extends the {@link Polygon} class, inheriting properties like vertices and their associated plane.
 * A {@code Triangle} is defined by exactly three vertices, forming a valid triangle.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a {@code Triangle} using three vertices.
     * This constructor delegates to the {@link Polygon} constructor and ensures that
     * the three given points form a valid triangle.
     *
     * @param a vertex defining the triangle
     * @param b vertex defining the triangle
     * @param c vertex defining the triangle
     * @throws IllegalArgumentException if the provided vertices violate the constraints
     *                                  enforced by {@link Polygon}'s constructor.
     */
    public Triangle(Point a, Point b, Point c) {
        super(a,b,c);
    }

    /**
     * Finds the intersection point between the given {@link Ray} and this triangle.
     * <p>
     * First computes the intersection with the triangle’s supporting plane. If an intersection
     * exists, it then checks whether the point lies inside the triangle using vector cross products
     * and consistent sign tests.
     *
     * @param ray the ray to intersect with the triangle
     * @return a list containing the intersection point if it lies inside the triangle; {@code null} otherwise
     */
    @Override
    public List<Point> findIntersections (Ray ray) {
        List<Point> intersections = this.plane.findIntersections(ray);
        if (intersections == null) {
            return null;
        }

        Point p0 = ray.getHead();
        Point p1 = vertices.get(0);
        Point p2 = vertices.get(1);
        Point p3 = vertices.get(2);

        Vector v = ray.getDirection();
        Vector v1 = p1.subtract(p0);
        Vector v2 = p2.subtract(p0);
        Vector v3 = p3.subtract(p0);

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double sign1 = v.dotProduct(n1);
        double sign2 = v.dotProduct(n2);
        double sign3 = v.dotProduct(n3);

        if ((sign1 > 0 && sign2 > 0 && sign3 > 0) ||
                (sign1 < 0 && sign2 < 0 && sign3 < 0)) {
            return intersections;
        }

        return null;
    }



}

