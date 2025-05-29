package primitives;

import geometries.Intersectable.Intersection;

import java.util.List;

import static primitives.Util.isZero;

/**
 * The {@code Ray} class represents a ray in 3-dimensional space. It is defined by a starting point (head)
 * and a direction vector. The ray extends infinitely in the direction of the vector, and the direction vector
 * is normalized. The class provides methods for working with rays in geometric operations.
 */

public class Ray {
    /**
     * Represents the starting point (head) of the ray in 3-dimensional space.
     * This is the origin point from which the ray extends infinitely in its direction.
     */
    private final Point head;

    /**
     * Represents the direction vector of the ray.
     * This vector specifies the direction in which the ray extends from its starting point.
     * The direction vector is normalized and cannot be a zero vector.
     */
    final Vector direction;

    /**
     * Constructs a {@code Ray} object using a starting point (head) and a direction vector.
     * The direction vector is normalized to ensure consistency in calculations.
     *
     * @param head the starting point of the ray
     * @param direction the direction vector of the ray
     *                  (will be automatically normalized)
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Gets the head of the ray
     *
     * @return point which is the head of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * Gets the direction of the ray
     *
     * @return vector which is direction of ray axis
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Calculates a point on the ray axis at a given distance from the head of the ray
     *
     * @param t distance from the head of the ray
     * @return point on the ray axis at the given distance from the head of the ray
     */
    public Point getPoint(double t) {
        if (t < 0) {
            return null;
        }

        if (Util.isZero(t)) {
            return head;
        }

        return head.add(direction.scale(t));
    }


    /**
     * Finds the closest point to the ray head out of a list of points
     *
     * @param points list of points to be checked
     * @return points from list closest to ray head
     */
    public Point findClosestPoint(List<Point> points) {
        return (points == null || points.isEmpty()) ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }

    /**
     * Finds the closest intersection point from a list of intersections.
     *
     * @param intersections a list of {@link Intersection} objects to evaluate
     * @return the closest {@link Intersection} to the ray origin, or {@code null} if the list is empty
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections.isEmpty())
            return null;

        Intersection closestIntersection = intersections.getFirst();
        for (Intersection intersection : intersections) {
            if (intersection.point.distance(this.head) < closestIntersection.point.distance(this.head)) {
                closestIntersection = intersection;
            }
        }
        return closestIntersection;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Ray other && this.head.equals(other.head) && this.direction.equals(other.direction);
    }
}
