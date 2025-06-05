package primitives;

/**
 * The {@code Point} class represents a point in 3-dimensional space.
 * It encapsulates the coordinates of the point using the {@link Double3} class, which holds the x, y, and z coordinates.
 * The class provides methods for point arithmetic (addition, subtraction) and distance calculations.
 */
public class Point {

    /** The coordinates of the point, represented by a {@link Double3} object (x, y, z). */
    protected Double3 xyz;

    /** A static constant representing the origin (0, 0, 0) of the 3D coordinate system. */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructs a {@code Point} with the specified coordinates.
     *
     * @param d1 the x-coordinate of the point
     * @param d2 the y-coordinate of the point
     * @param d3 the z-coordinate of the point
     */
    public Point(double d1, double d2, double d3) {
        this.xyz = new Double3(d1, d2, d3);
    }

    /**
     * Constructs a {@code Point} using a {@link Double3} object that contains the coordinates.
     *
     * @param xyz the {@link Double3} object representing the point's coordinates
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Subtracts the coordinates of the given point from this point and returns the resulting vector.
     * The subtraction of two points results in a vector.
     *
     * @param point the point to subtract from this point
     * @return a {@link Vector} representing the difference between the two points
     */
    public Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    /**
     * Adds a given {@link Vector} to this point and returns a new point representing the result.
     *
     * @param vector the vector to add to this point
     * @return a new {@code Point} representing the result of the addition
     */
    public Point add(Vector vector) {
        return new Point(this.xyz.add(vector.xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     * The squared distance is the sum of the squared differences of their corresponding coordinates.
     *
     * @param point the other point to calculate the squared distance to
     * @return the squared distance between this point and the provided point
     */
    public double distanceSquared(Point point) {
        return (xyz.d1() - point.xyz.d1()) * (xyz.d1() - point.xyz.d1()) +
                (xyz.d2() - point.xyz.d2()) * (xyz.d2() - point.xyz.d2()) +
                (xyz.d3() - point.xyz.d3()) * (xyz.d3() - point.xyz.d3());
    }

    /**
     * Calculates the Euclidean distance between this point and another point.
     * This is the square root of the squared distance.
     *
     * @param point the other point to calculate the distance to
     * @return the Euclidean distance between this point and the provided point
     */
    public double distance(Point point) {
        return Math.sqrt(this.distanceSquared(point));
    }

    @Override
    public String toString() {
        return "Point{" + "xyz=" + xyz + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Point other && this.xyz.equals(other.xyz);
    }

    public double getX() {
        return xyz.d1();
    }
    public double getY() {
        return xyz.d2();
    }
    public double getZ() {
        return xyz.d3();
    }
    public Double3 getXYZ() {
        return xyz;
    }
}
