package primitives;

/**
 * The {@code Vector} class represents a vector in 3-dimensional space.
 * It extends the {@link Point} class to inherit the coordinates and provides vector-specific operations
 * such as addition, scaling, dot product, cross product, and normalization.
 * Vectors cannot be zero vectors; an exception is thrown when attempting to create a zero vector.
 */
public class Vector extends Point {
    public static final Vector AXIS_X = new Vector(1, 0, 0);
    public static final Vector AXIS_Y = new Vector(0, 1, 0);
    public static final Vector AXIS_Z = new Vector(0, 0, 1);
    /**
     * Constructs a {@code Vector} using three double values for its coordinates.
     * Throws an {@link IllegalArgumentException} if the vector is a zero vector (i.e., all components are zero).
     *
     * @param d1 the x-coordinate of the vector
     * @param d2 the y-coordinate of the vector
     * @param d3 the z-coordinate of the vector
     * @throws IllegalArgumentException if the vector is a zero vector
     */
    public Vector(double d1, double d2, double d3) {
        super(d1, d2, d3);
        if (this.xyz.equals(Point.ZERO.xyz)) {
            throw new IllegalArgumentException("Vector cannot be zero");
        }
    }

    /**
     * Constructs a {@code Vector} with the specified {@link Double3} coordinates.
     * Throws an exception if the provided coordinates represent a zero vector.
     *
     * @param xyz the {@link Double3} object containing the coordinates for the vector
     * @throws IllegalArgumentException if the provided coordinates represent a zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.d1()==0 && xyz.d2()==0 && xyz.d3()==0) {
            throw new IllegalArgumentException("Vector cannot be zero");
        }
    }

    /**
     * Adds the given vector to this vector and returns a new vector representing the result.
     *
     * @param vector the vector to be added to this vector
     * @return a new {@code Vector} that is the result of adding the two vectors
     */
    public Vector add(Vector vector) {
        return new Vector(this.xyz.add(vector.xyz));
    }

    /**
     * Scales this vector by a given scalar value.
     * The scaling operation multiplies each component of the vector by the scalar.
     *
     * @param scalar the scalar value by which to multiply the vector components
     * @return a new {@code Vector} instance representing the scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(this.xyz.scale(scalar));
    }

    /**
     * Calculates the dot product of this vector with another vector.
     * The dot product is the sum of the products of their corresponding components.
     *
     * @param vector the vector to calculate the dot product with
     * @return the dot product of this vector and the specified vector
     */
    public double dotProduct(Vector vector) {
        return this.xyz.d1() * vector.xyz.d1() + this.xyz.d2() * vector.xyz.d2() + this.xyz.d3() * vector.xyz.d3();
    }

    /**
     * Computes the cross product of this vector with another vector.
     * The cross product of two vectors results in a new vector perpendicular to the plane formed by the input vectors.
     * The formula for the cross product is:
     *
     * crossProduct(A, B) = (Ay * Bz - Az * By, Az * Bx - Ax * Bz, Ax * By - Ay * Bx)
     *
     * @param vector the other vector to compute the cross product with
     * @return a new {@code Vector} representing the cross product of this vector and the specified vector
     * @throws IllegalArgumentException if the resulting vector is a zero vector
     */
    public Vector crossProduct(Vector vector) {
        return new Vector(
                this.xyz.d2() * vector.xyz.d3() - this.xyz.d3() * vector.xyz.d2(),
                this.xyz.d3() * vector.xyz.d1() - this.xyz.d1() * vector.xyz.d3(),
                this.xyz.d1() * vector.xyz.d2() - this.xyz.d2() * vector.xyz.d1()
        );
    }

    /**
     * Calculates the squared length (magnitude) of the vector.
     * This is equivalent to the dot product of the vector with itself.
     *
     * @return the squared length of the vector
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Calculates the length (magnitude) of the vector.
     * The length is the square root of the squared length of the vector.
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes the vector to produce a unit vector.
     * A unit vector has a magnitude of 1 while maintaining the same direction as the original vector.
     *
     * @return a new {@code Vector} instance representing the normalized version of the current vector
     */
    public Vector normalize() {
        return this.scale(1.0 / this.length());
    }

    @Override
    public String toString() {
        return "Vector{" + super.toString() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Vector other && super.equals(o);
    }


}
