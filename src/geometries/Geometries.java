package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Geometries} class represents a collection of geometric objects.
 * It implements the {@link Intersectable} interface, allowing intersection queries
 * with all contained geometries as a group.
 * <p>
 * This class supports adding multiple geometries and finding all intersection points
 * between a given {@link Ray} and the geometries in the collection.
 */
public class Geometries implements Intersectable {
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Constructs an empty collection of geometries.
     */
    public Geometries() {}

    /**
     * Constructs a collection containing the specified geometries.
     *
     * @param geometries the geometries to add to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     *
     * @param geometries the geometries to add
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
    }

    /**
     * Finds all intersection points between the given {@link Ray} and the geometries in the collection.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of intersection points, or {@code null} if there are none
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Point> intersectionsByGeometry = geometry.findIntersections(ray);
            if (intersectionsByGeometry != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();
                }
                intersections.addAll(intersectionsByGeometry);
            }
        }
        return intersections;
    }
}
