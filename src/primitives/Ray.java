package primitives;

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
