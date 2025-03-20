package geometries;

/**
 * {@code RadialGeometry} is an abstract class that extends {@link Geometry}
 * to represent geometric objects characterized by a radius.
 * This class serves as a base for all radial geometries, such as spheres, cylinders, and tubes.
 */
public abstract class RadialGeometry extends Geometry {

    /**
     * The radius of the radial geometry.
     * This value is immutable and must be non-negative.
     * It defines the size or extent of the radial object and is used by subclasses
     * for various geometric computations.
     */
    protected final double radius;

    /**
     * Constructs a {@code RadialGeometry} object with the specified radius.
     *
     * @param radius the radius of the radial geometry; must be non-negative
     * @throws IllegalArgumentException if {@code radius} is negative
     */
    protected RadialGeometry(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }
        this.radius = radius;
    }
}

