package geometries;

import primitives.Point;

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
     * @param vertices the three vertices defining the triangle
     * @throws IllegalArgumentException if the provided vertices violate the constraints
     *                                  enforced by {@link Polygon}'s constructor.
     */
    public Triangle(Point a, Point b, Point c) {
        super(a,b,c);
    }



}

